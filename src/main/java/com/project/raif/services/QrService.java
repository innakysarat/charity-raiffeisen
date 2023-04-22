package com.project.raif.services;

import com.project.raif.exception.ApiException;
import com.project.raif.exception.ErrorCode;
import com.project.raif.models.dto.qr.QrApiRequest;
import com.project.raif.models.dto.qr.QrFrontRequest;
import com.project.raif.models.dto.qr.QrResponse;
import com.project.raif.models.dto.subscription.Subscription;
import com.project.raif.models.entity.Qr;
import com.project.raif.models.enums.CurrencyType;
import com.project.raif.models.enums.QrType;
import com.project.raif.repositories.QrRepository;
import com.project.raif.services.clients.RaifQrClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class QrService {
    private final QrRepository qrRepository;
    private final RaifQrClient qrClient;

    public QrResponse registerQr(QrFrontRequest frontRequest) {
        QrApiRequest apiRequest = QrApiRequest.builder()
                .amount(frontRequest.getAmount())
                .sbpMerchantId(frontRequest.getSbpMerchantId())
                .qrExpirationDate(ZonedDateTime.now().plusHours(1))
                .order(UUID.randomUUID().toString())
                .currency(CurrencyType.RUB)
                .qrType(QrType.QRDynamic)
                .build();
        if (frontRequest.getSubscription() != null) {
            apiRequest.setSubscription(new Subscription(UUID.randomUUID().toString(),
                    frontRequest.getSubscription().getSubscriptionPurpose()));
        }
        QrResponse apiResponse = qrClient.getQr(apiRequest);

        Qr qrEntity = new Qr(frontRequest.getSbpMerchantId(), frontRequest.getAmount(),
                apiRequest.getQrExpirationDate(), apiResponse);
        try {
            qrEntity = qrRepository.save(qrEntity);
        } catch (Exception ex) {
            log.error("Got exception while saving qr", ex);
        }
        apiResponse.qrId = qrEntity.getId().toString();
        return apiResponse;
    }

    public String getPaymentInfo(Long qrId) {
        Qr qr = qrRepository.findById(qrId).orElseThrow(() ->
                new ApiException(ErrorCode.ERROR_NOT_FOUND_QR, ErrorCode.ERROR_NOT_FOUND_QR.getMessage()));
        return qr.getQrStatus();
    }
}
