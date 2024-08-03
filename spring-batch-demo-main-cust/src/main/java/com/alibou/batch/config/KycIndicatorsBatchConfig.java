package com.alibou.batch.config;

import com.alibou.batch.entity.CustIndicator;
import com.alibou.batch.mapper.KycIndicatorsLineMapper;
import com.alibou.batch.processor.KycIndicatorsProcessor;
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
public class KycIndicatorsBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final CustIndicatorRepository custIndicatorRepository;
    private final KycIndicatorsLineMapper kycIndicatorsLineMapper;
    private final KycIndicatorsProcessor kycIndicatorsProcessor;

    @Bean
    public FlatFileItemReader<CustIndicator> kycFileReader() {
        FlatFileItemReader<CustIndicator> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/kyc_indicators.csv"));
        itemReader.setName("kycFileReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(kycIndicatorsLineMapper.lineMapper());
        return itemReader;
    }

    @Bean
    public RepositoryItemWriter<CustIndicator> kycFileWriter() {
        RepositoryItemWriter<CustIndicator> writer = new RepositoryItemWriter<>();
        writer.setRepository(custIndicatorRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step kycStep() {
        return new StepBuilder("kycStep", jobRepository)
                .<CustIndicator, CustIndicator>chunk(10, platformTransactionManager)
                .reader(kycFileReader())
                .processor(kycIndicatorsProcessor)
                .writer(kycFileWriter())
                .build();
    }

    @Bean
    public Job kycIndicatorsJob() {
        return new JobBuilder("kycIndicatorsJob", jobRepository)
                .start(kycStep())
                .build();
    }
}
