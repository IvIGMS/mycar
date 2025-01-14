package com.mycar.business.process;

import com.mycar.business.controllers.constants.NotificationTypeConstants;
import com.mycar.business.entities.NotificationEntity;
import com.mycar.business.services.IssueService;
import com.mycar.business.services.NotificationService;
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

import java.util.List;

@Configuration
@EnableBatchProcessing
public class UpdateIssuesByDateProcess {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private IssueService issueService;

    @Autowired
    private NotificationService notificationService;

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

                    List<Long> issuesId = issueService.updateIssuesExpiredByDate();

                    chunkContext.getStepContext().getStepExecution()
                            .getJobExecution()
                            .getExecutionContext()
                            .put("issues", issuesId);

                    return null;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step createNotifications() {
        return new StepBuilder("createNotifications", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    List<Long> issuesId = (List<Long>) chunkContext.getStepContext().getStepExecution()
                            .getJobExecution()
                            .getExecutionContext()
                            .get("issues");

                    // Create notifications
                    List<NotificationEntity> notifications = notificationService.createNotifications(
                            issuesId,
                            NotificationTypeConstants.NT_ISSUE_BY_DATE_ID
                    );

                    // Send notifications
                    notifications.forEach(n -> {
                        notificationService.sendNotification(n);
                    });

                    return null;
                }, transactionManager)
                .build();
    }
}
