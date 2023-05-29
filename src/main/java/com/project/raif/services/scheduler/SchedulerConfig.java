package com.project.raif.services.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Bean
    public CheckStatusScheduler checkPaymentStatusScheduler(CheckStatus checkStatus)
    {
        return new CheckStatusScheduler(checkStatus);
    }
}