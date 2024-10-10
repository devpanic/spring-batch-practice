package com.devpanic.batchprj.config;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class HelloJobParameterValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        if(jobParameters == null || jobParameters.getParameters().size() != 1)
            throw new JobParametersInvalidException("hello job require 1 parameter");
        String name = jobParameters.getString("name1");

        if(name == null || name.isEmpty()) throw new JobParametersInvalidException("hello job name1 required");
        if(!name.equals("miyeon")) throw new JobParametersInvalidException("hello job name not equals miyeon");
    }
}
