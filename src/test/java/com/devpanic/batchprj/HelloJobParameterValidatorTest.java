package com.devpanic.batchprj;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.devpanic.batchprj.config.BatchConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
public class HelloJobParameterValidatorTest {
    private JobLauncherTestUtils jobLauncherTestUtils;
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    private BatchConfig batchConfig;

    @Autowired
    public HelloJobParameterValidatorTest(JobLauncherTestUtils jobLauncherTestUtils, JobRepositoryTestUtils jobRepositoryTestUtils, BatchConfig batchConfig) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
        this.jobRepositoryTestUtils = jobRepositoryTestUtils;
        this.batchConfig = batchConfig;
    }

    @BeforeEach
    void clearJobExecutions() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    @DisplayName("helloJob 실행 성공")
    void helloJobParameterValidationSuccess(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name1", "miyeon")
                .toJobParameters();

        jobLauncherTestUtils.setJob(batchConfig.helloWorldJob());

        assertDoesNotThrow(() -> {
            jobLauncherTestUtils.launchJob(jobParameters);
        });
    }

    @Test
    @DisplayName("parameter 갯수가 맞지 않는 경우")
    void invalidParameterCount(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("invalid1", "invalid1")
                .addString("invalid2", "invalid2")
                .toJobParameters();

        jobLauncherTestUtils.setJob(batchConfig.helloWorldJob());

        assertThatThrownBy(() -> {
                jobLauncherTestUtils.launchJob(jobParameters);
            }).isInstanceOf(JobExecutionException.class)
            .hasMessageContaining("hello job require 1 parameter");
    }

    @Test
    @DisplayName("name1이 존재하지 않는 경우")
    void notExistName1(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("invalid1", "invalid1")
                .toJobParameters();

        jobLauncherTestUtils.setJob(batchConfig.helloWorldJob());

        assertThatThrownBy(() -> {
                jobLauncherTestUtils.launchJob(jobParameters);
            }).isInstanceOf(JobExecutionException.class)
            .hasMessageContaining("hello job name1 required");
    }

    @Test
    @DisplayName("name1이 miyeon과 같지 않은 경우")
    void notSameName1AndMiyeon(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name1", "miyeonnn")
                .toJobParameters();

        jobLauncherTestUtils.setJob(batchConfig.helloWorldJob());

        assertThatThrownBy(() -> {
                jobLauncherTestUtils.launchJob(jobParameters);
            }).isInstanceOf(JobExecutionException.class)
            .hasMessageContaining("hello job name not equals miyeon");
    }
}
