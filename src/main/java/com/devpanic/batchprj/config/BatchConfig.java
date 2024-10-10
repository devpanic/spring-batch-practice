package com.devpanic.batchprj.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job helloWorldJob(Step helloWorldStep) {
        return new JobBuilder("helloJob", jobRepository)
                .start(helloWorldStep)
                .build();
    }

    @Bean
    public Job goodbyeWorldJob(Step goodbyeWorldStep) {
        return new JobBuilder("goodbyeJob", jobRepository)
                .start(goodbyeWorldStep)
                .build();
    }

    @Bean
    public Step helloWorldStep(Tasklet helloWorldTasklet) {
        return new StepBuilder("helloStep", jobRepository)
                .tasklet(helloWorldTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step goodbyeWorldStep(Tasklet goodbyeWorldTasklet) {
        return new StepBuilder("goodbyeStep", jobRepository)
                .tasklet(goodbyeWorldTasklet, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet helloWorldTasklet(@Value("#{jobParameters[name1]}") String name) {
        return (contribution, chunkContext) -> {
            System.out.println("Hello, " + (name != null ? name : "World") + "!");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope
    public Tasklet goodbyeWorldTasklet(@Value("#{jobParameters[name2]}") String name) {
        return (contribution, chunkContext) -> {
            System.out.println("Goodbye, " + (name != null ? name : "World") + "!");
            return RepeatStatus.FINISHED;
        };
    }
}