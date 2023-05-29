package com.project.raif.services.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
public class CheckStatusScheduler {

    private CheckStatus checkStatus;

    @Scheduled(cron = "*/2 * * * * *")
    void checkPayment() {
        checkStatus.checkPaymentInfo();
    }
    @Scheduled(cron = "*/5 * * * * *")
    void checkSubscriptionInfo() {
        checkStatus.checkSubscriptionInfo();
    }
}