Sure! Let's modify the code as per your requirement by separating the reader class from the batch service class while maintaining a proper structure for your batch processing job.

I'll break down your code into three distinct components:

KYC Indicators Reader Class (responsible for reading the KYC data from a CSV file).
KYC Indicators Batch Configuration (configures the job, step, and reader).
Batch Service Class (manages scheduling and job execution logic).
1. KYC Indicators Reader Class (Reader for KYC data)
java
Copy code
package com.ivr.batch.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import com.ivr.batch.dto.KycIndicatorsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class KycIndicatorsReader {

    private static final Logger log = LoggerFactory.getLogger(KycIndicatorsReader.class);

    @Bean
    public FlatFileItemReader<KycIndicatorsDto> kycFileReader(String inputFile) {
        log.info("Starting kycFileReader...");
        log.info("KYC Indicator Input file is {}", inputFile);

        FlatFileItemReader<KycIndicatorsDto> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource(inputFile));
        itemReader.setName("kycFileReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());

        log.info("Reader setup completed.");
        return itemReader;
    }

    @Bean
    public DefaultLineMapper<KycIndicatorsDto> lineMapper() {
        log.info("Setting up Line Mapper...");

        DefaultLineMapper<KycIndicatorsDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("relId", "fKycStatus");

        BeanWrapperFieldSetMapper<KycIndicatorsDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(KycIndicatorsDto.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        log.info("Line Mapper setup complete.");
        return lineMapper;
    }
}
2. KYC Indicators Batch Configuration Class
java
Copy code
package com.ivr.batch.config;

import com.ivr.batch.reader.KycIndicatorsReader;
import com.ivr.batch.dto.KycIndicatorsDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class KycIndicatorsBatchConfig {

    @Autowired
    private KycIndicatorsReader kycIndicatorsReader;

    @Autowired
    private KycIndicatorsProcessor kycIndicatorsProcessor; // Assuming you have a processor class

    @Autowired
    private KycIndicatorsWriter kycIndicatorsWriter; // Assuming you have a writer class

    @Autowired
    private KycJobListener kycJobListener; // Assuming you have a listener

    @Bean
    public Step kycStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws SQLException {
        return new StepBuilderFactory(jobRepository, transactionManager)
                .get("kycStep")
                .<KycIndicatorsDto, CustIndicator>chunk(100)
                .reader(kycIndicatorsReader.kycFileReader("inputFile"))
                .processor(kycIndicatorsProcessor)
                .writer(kycIndicatorsWriter)
                .listener(kycJobListener)
                .build();
    }

    @Bean
    public Job kycIndicatorsJob(Step kycStep, JobRepository jobRepository) {
        return new JobBuilderFactory(jobRepository)
                .get("kycIndicatorsJob")
                .start(kycStep)
                .build();
    }
}
3. Batch Service Class
java
Copy code
package com.ivr.batch.service;

import com.ivr.batch.entity.CemsAuditLog;
import com.ivr.batch.repo.CemsAuditLogEnityRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.SQLException;
import java.util.Date;

@Service
public class BatchService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private CemsAuditLogEnityRepository cemsAuditLogEnityRepository;

    @Autowired
    @Qualifier("kycIndicatorsJob")
    private Job kycIndicatorsJob;

    // Scheduler for KYC Indicator Batch Job
    @Scheduled(cron = "0 0/1 * * * ?")
    public void importKycIndicators() throws IOException, SQLException {
        CemsAuditLog cemsAuditLog = getLatestSubmittedFiles();
        if (cemsAuditLog == null) {
            throw new IllegalArgumentException("No valid file found in the database");
        }

        if (cemsAuditLog.getInputfileContent() == null) {
            throw new IllegalArgumentException("No input file found for the selected record");
        }

        // Convert Blob to a temporary CSV file
        File tempFile = File.createTempFile("kycData", ".csv");
        tempFile.deleteOnExit();

        try (InputStream inputStream = cemsAuditLog.getInputfileContent().getBinaryStream();
             OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        runJob(kycIndicatorsJob, "KYC Indicators", tempFile.getAbsolutePath());
    }

    private CemsAuditLog getLatestSubmittedFiles() {
        return cemsAuditLogEnityRepository.findLatestKycIndSubmittedFiles("CustInd", "KYCInd").stream().findFirst().orElse(null);
    }

    private void runJob(Job job, String jobName, String filePath) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addString("jobName", jobName)
                .addString("input.file", filePath)
                .addDate("launchDate", new Date());

        try {
            JobExecution run = jobLauncher.run(job, jobParametersBuilder.toJobParameters());
            String status = run.getStatus().toString();
            if ("COMPLETED".equalsIgnoreCase(status)) {
                // Update success log
                updateBatchCompletedStatus(cemsAuditLog.getFileId().toString());
            } else {
                updateBatchFailureStatus(cemsAuditLog.getFileId().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBatchCompletedStatus(String fileId) {
        // Implementation for updating batch completion status
    }

    private void updateBatchFailureStatus(String fileId) {
        // Implementation for updating batch failure status
    }
}
Explanation:
Reader Class (KycIndicatorsReader): Contains the logic for reading the CSV file.
Batch Config Class (KycIndicatorsBatchConfig): Defines the job and step configuration.
Batch Service Class (BatchService): Schedules the jobs and handles the job execution logic.
This separation will make the code more maintainable and modular, enabling easy modifications and testing.










