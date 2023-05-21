package com.project.raif.services.scheduler;

import com.project.raif.models.dto.qr.QrPaymentResponse;
import com.project.raif.models.dto.subscription.SubscriptionInfoResponse;
import com.project.raif.models.entity.Qr;
import com.project.raif.models.enums.PaymentStatus;
import com.project.raif.models.enums.QrStatus;
import com.project.raif.models.enums.SubscriptionStatus;
import com.project.raif.repositories.QrRepository;
import com.project.raif.services.clients.RaifQrClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CheckPaymentStatusService implements CheckPaymentStatus {

    private final QrRepository qrRepository;
    private final RaifQrClient qrClient;

    @Transactional
    @Override
    public void checkPaymentInfo() {
        List<Qr> qrList = qrRepository.findAllByQrStatus(QrStatus.NEW);
        for (Qr qr : qrList) {
            try {
                ZonedDateTime lt = ZonedDateTime.now();
                if (lt.compareTo(qr.getQrExpirationDate()) >= 0) {
                    qr.setQrStatus(QrStatus.EXPIRED);
                    qrRepository.save(qr);
                    continue;
                }
                QrPaymentResponse response = qrClient.getPaymentInfo(qr.getQrId());
                PaymentStatus paymentStatus = response.getPaymentStatus();
                if (paymentStatus == null) {
                    log.error("No payment info. Can't get payment status");
                    continue;
                }
                switch (paymentStatus) {
                    case SUCCESS -> {
                        qr.setQrStatus(QrStatus.PAID);
                        qr.setQrPaymentDate(LocalDate.now());
                        qr.setSubscriptionStatus(SubscriptionStatus.SUBSCRIBED);
                        qrRepository.save(qr);
                    }
                    case DECLINED -> {
                        qr.setQrStatus(QrStatus.CANCELLED);
                        qrRepository.save(qr);
                    }
                    default -> {
                    }
                }
            } catch (Exception ex) {
                log.error("Got exception from raif client", ex);
            }
        }
    }

    @Transactional
    @Override
    public void checkSubscriptionInfo() {
        List<Qr> qrsSubscription = qrRepository.findAllBySubscriptionStatus(SubscriptionStatus.INACTIVE);
        for (Qr qr : qrsSubscription) {
            try {
                SubscriptionInfoResponse response = qrClient.getSubscriptionInfo(qr.getSubscriptionId());
                SubscriptionStatus subscriptionStatus = response.getStatus();
                if (subscriptionStatus == null) {
                    log.error("No subscription info. Can't get subscription status");
                    continue;
                }
                switch (subscriptionStatus) {
                    case INACTIVE -> {
                        qr.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
                        qrRepository.save(qr);
                    }
                    case SUBSCRIBED -> {
                        qr.setSubscriptionStatus(SubscriptionStatus.SUBSCRIBED);
                        qrRepository.save(qr);
                    }
                    case UNSUBSCRIBED -> {
                        qr.setSubscriptionStatus(SubscriptionStatus.UNSUBSCRIBED);
                        qrRepository.save(qr);
                    }
                    default -> {
                    }
                }
            } catch (Exception ex) {
                log.error("Got exception from raif client", ex);
            }
        }
    }
}