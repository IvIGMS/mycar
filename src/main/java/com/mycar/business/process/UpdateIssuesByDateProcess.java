package com.mycar.business.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycar.business.entities.IssueEntity;
import com.mycar.business.entities.NotificationEntity;
import com.mycar.business.entities.StatusEntity;
import com.mycar.business.repositories.IssueRepository;
import com.mycar.business.repositories.NotificationRepository;
import com.mycar.business.services.NotificationTypeService;
import com.mycar.business.services.impl.KafkaProducerServiceImpl;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class UpdateIssuesByDateProcess {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationTypeService notificationTypeService;

    @Autowired
    private KafkaProducerServiceImpl kafkaProducerService;

    @Autowired
    private ObjectMapper objectMapper;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public UpdateIssuesByDateProcess(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean(name = "process")
    public Job updateIssuesJob() {
        return new JobBuilder("updateIssuesJob", jobRepository)
                .start(updateIssues())
                .next(createNotifications())
                .build();
    }

    @Bean
    public Step updateIssues() {
        return new StepBuilder("updateIssues", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    List<IssueEntity> issues = issueRepository.getIssuesByExpiredDate(
                            LocalDate.now().atStartOfDay(),
                            LocalDate.now().plusDays(1).atStartOfDay()
                    );

                    chunkContext.getStepContext().getStepExecution()
                            .getJobExecution()
                            .getExecutionContext()
                            .put("issues", issues.stream().map(IssueEntity::getId).toList());

                    issues.forEach(issue -> {
                        issue.setStatusEntity(StatusEntity.builder().id(0L).statusName("status.finish").build());
                        issueRepository.save(issue);
                    });

                    return null;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step createNotifications() {
        return new StepBuilder("createNotifications", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    List<Long> issues = (List<Long>) chunkContext.getStepContext().getStepExecution()
                            .getJobExecution()
                            .getExecutionContext()
                            .get("issues");

                    List<IssueEntity> allIssues = issueRepository.getIssuesByIds(issues);

                    List<NotificationEntity> notifications = allIssues.stream().map(issue -> NotificationEntity.builder()
                                .header("Notificación de mantenimiento vencido")
                                .message("El mantenimiento " + issue.getName() + " ha vencido.")
                                .sender("admin@mycar.com")
                                .receiver(issue.getCarEntity().getUserEntity().getEmail())
                                .notificationTypeEntity(notificationTypeService.findById(101L))
                                .build()
                    ).toList();

                    notificationRepository.saveAll(notifications);

                    // Send notification
                    notifications.forEach(n -> {
                        try {
                            kafkaProducerService.sendMessage(objectMapper.writeValueAsString(n));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    return null;
                }, transactionManager)
                .build();
    }
}
