package com.mycar.business.process;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job updateIssuesByDateProcessJob;

    public JobScheduler(JobLauncher jobLauncher, @Qualifier("process") Job updateIssuesByDateProcessJob) {
        this.jobLauncher = jobLauncher;
        this.updateIssuesByDateProcessJob = updateIssuesByDateProcessJob;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void scheduleJob() {
        try {
            jobLauncher.run(updateIssuesByDateProcessJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

