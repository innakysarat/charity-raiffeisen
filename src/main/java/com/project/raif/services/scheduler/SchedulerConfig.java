package com.project.raif.services.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Bean
    public CheckPaymentStatusScheduler checkPaymentStatusScheduler(CheckPaymentStatus checkPaymentStatus)
    {
        return new CheckPaymentStatusScheduler(checkPaymentStatus);
    }
}