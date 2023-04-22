package com.project.raif.services.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
public class CheckPaymentStatusScheduler {

    private CheckPaymentStatus checkPaymentStatus;

    @Scheduled(cron = "*/2 * * * * *")
    void checkPayment() {
        checkPaymentStatus.checkInfo();
    }
}