package com.alibou.batch.config;

import com.alibou.batch.entity.CustIndicator;
import com.alibou.batch.mapper.SensitiveCustomerLineMapper;
import com.alibou.batch.processor.SensitiveCustomerProcessor;
import com.alibou.batch.repo.CustIndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SensitiveCustomerBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final CustIndicatorRepository custIndicatorRepository;
    private final SensitiveCustomerLineMapper sensitiveCustomerLineMapper;
    private final SensitiveCustomerProcessor sensitiveCustomerProcessor;

    @Bean
    public FlatFileItemReader<CustIndicator> sensitiveCustomerFileReader() {
        FlatFileItemReader<CustIndicator> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/sensitive_customer.csv"));
        itemReader.setName("sensitiveCustomerFileReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(sensitiveCustomerLineMapper.lineMapper());
        return itemReader;
    }

    @Bean
    public RepositoryItemWriter<CustIndicator> sensitiveCustomerFileWriter() {
        RepositoryItemWriter<CustIndicator> writer = new RepositoryItemWriter<>();
        writer.setRepository(custIndicatorRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step sensitiveCustomerStep() {
        return new StepBuilder("sensitiveCustomerStep", jobRepository)
                .<CustIndicator, CustIndicator>chunk(10, platformTransactionManager)
                .reader(sensitiveCustomerFileReader())
                .processor(sensitiveCustomerProcessor)
                .writer(sensitiveCustomerFileWriter())
                .build();
    }

    @Bean
    public Job sensitiveCustomerJob() {
        return new JobBuilder("sensitiveCustomerJob", jobRepository)
                .start(sensitiveCustomerStep())
                .build();
    }
}
