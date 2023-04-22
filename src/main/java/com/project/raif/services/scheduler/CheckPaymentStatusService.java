package com.project.raif.services.scheduler;

import com.project.raif.models.dto.qr.QrPaymentResponse;
import com.project.raif.models.entity.Qr;
import com.project.raif.models.enums.QrStatus;
import com.project.raif.repositories.QrRepository;
import com.project.raif.services.clients.RaifQrClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public void checkInfo() {
        List<Qr> qrList = qrRepository.findAllByQrStatus(QrStatus.NEW.toString());
        for (Qr qr : qrList) {
            try {
                ZonedDateTime lt = ZonedDateTime.now();
                if (lt.compareTo(qr.getQrExpirationDate()) >= 0) {
                    qr.setQrStatus(QrStatus.EXPIRED.name());
                    qrRepository.save(qr);
                    continue;
                }
                QrPaymentResponse response = qrClient.getPaymentInfo(qr.getQrId());
                String paymentStatus = response.getPaymentStatus();
                if (paymentStatus == null) {
                    log.error("No payment info. Can't get payment status");
                    continue;
                }
                switch (paymentStatus) {
                    case "SUCCESS":
                        qr.setQrStatus(QrStatus.PAID.name());
                        qrRepository.save(qr);
                        break;
                    case "DECLINED":
                        qr.setQrStatus(QrStatus.CANCELLED.name());
                        qrRepository.save(qr);
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {
                log.error("Got exception from raif client", ex);
            }
        }
    }
}