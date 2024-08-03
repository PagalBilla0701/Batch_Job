package com.alibou.batch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job kycIndicatorsJob;
    private final Job transferExclusionJob;
    private final Job sensitiveCustomerJob;
    private final Job rbsCustomerJob;

    public BatchController(JobLauncher jobLauncher,
                           @Qualifier("kycIndicatorsJob") Job kycIndicatorsJob,
                           @Qualifier("transferExclusionJob") Job transferExclusionJob,
                           @Qualifier("sensitiveCustomerJob") Job sensitiveCustomerJob,
                           @Qualifier("importRbsCustomerJob") Job rbsCustomerJob) {
        this.jobLauncher = jobLauncher;
        this.kycIndicatorsJob = kycIndicatorsJob;
        this.transferExclusionJob = transferExclusionJob;
        this.sensitiveCustomerJob = sensitiveCustomerJob;
        this.rbsCustomerJob = rbsCustomerJob;
    }

    @PostMapping("/kyc-indicators")
    public void importKycIndicators() {
        runJob(kycIndicatorsJob, "KYC Indicators");
    }

    @PostMapping("/transfer-exclusion")
    public void importTransferExclusion() {
        runJob(transferExclusionJob, "Transfer Exclusion");
    }

    @PostMapping("/sensitive-customer")
    public void importSensitiveCustomer() {
        runJob(sensitiveCustomerJob, "Sensitive Customer");
    }

    @PostMapping("/rbs-customer")
    public void importRbsCustomer() {
        runJob(rbsCustomerJob, "RBS Customer");
    }

    private void runJob(Job job, String jobName) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addString("jobName", jobName)
                .toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException
                 | JobRestartException
                 | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
