package com.alibou.batch.config;

import com.alibou.batch.entity.CustIndicator;
import com.alibou.batch.mapper.TransferExclusionLineMapper;
import com.alibou.batch.processor.TransferExclusionProcessor;
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
public class TransferExclusionBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final CustIndicatorRepository custIndicatorRepository;
    private final TransferExclusionLineMapper transferExclusionLineMapper;
    private final TransferExclusionProcessor transferExclusionProcessor;

    @Bean
    public FlatFileItemReader<CustIndicator> transferExclusionFileReader() {
        FlatFileItemReader<CustIndicator> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/transfer_exclusion.csv"));
        itemReader.setName("transferExclusionFileReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(transferExclusionLineMapper.lineMapper());
        return itemReader;
    }

    @Bean
    public RepositoryItemWriter<CustIndicator> transferExclusionFileWriter() {
        RepositoryItemWriter<CustIndicator> writer = new RepositoryItemWriter<>();
        writer.setRepository(custIndicatorRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step transferExclusionStep() {
        return new StepBuilder("transferExclusionStep", jobRepository)
                .<CustIndicator, CustIndicator>chunk(10, platformTransactionManager)
                .reader(transferExclusionFileReader())
                .processor(transferExclusionProcessor)
                .writer(transferExclusionFileWriter())
                .build();
    }

    @Bean
    public Job transferExclusionJob() {
        return new JobBuilder("transferExclusionJob", jobRepository)
                .start(transferExclusionStep())
                .build();
    }
}
