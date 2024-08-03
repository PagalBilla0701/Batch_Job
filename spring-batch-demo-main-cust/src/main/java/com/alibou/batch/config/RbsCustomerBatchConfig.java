package com.alibou.batch.config;

import com.alibou.batch.entity.CustIndicator;
import com.alibou.batch.mapper.RbsCustomerLineMapper;
import com.alibou.batch.processor.RbsCustomerProcessor;
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
public class RbsCustomerBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final CustIndicatorRepository custIndicatorRepository;
    private final RbsCustomerLineMapper rbsCustomerLineMapper;
    private final RbsCustomerProcessor rbsCustomerProcessor;

    @Bean
    public FlatFileItemReader<CustIndicator> rbsCustomerFileReader() {
        FlatFileItemReader<CustIndicator> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/rbs_customer.csv"));
        itemReader.setName("rbsCustomerFileReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(rbsCustomerLineMapper.lineMapper());
        return itemReader;
    }

    @Bean
    public RepositoryItemWriter<CustIndicator> rbsCustomerFileWriter() {
        RepositoryItemWriter<CustIndicator> writer = new RepositoryItemWriter<>();
        writer.setRepository(custIndicatorRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step rbsCustomerStep() {
        return new StepBuilder("rbsCustomerStep", jobRepository)
                .<CustIndicator, CustIndicator>chunk(10, platformTransactionManager)
                .reader(rbsCustomerFileReader())
                .processor(rbsCustomerProcessor)
                .writer(rbsCustomerFileWriter())
                .build();
    }

    @Bean
    public Job importRbsCustomerJob() {
        return new JobBuilder("importRbsCustomerJob", jobRepository)
                .start(rbsCustomerStep())
                .build();
    }
}
