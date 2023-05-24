package com.project.raif.services;

import com.project.raif.exception.ApiException;
import com.project.raif.exception.ErrorCode;
import com.project.raif.models.dto.qr.QrApiRequest;
import com.project.raif.models.dto.qr.QrFrontRequest;
import com.project.raif.models.dto.qr.QrResponse;
import com.project.raif.models.dto.subscription.QrSubscriptionRequest;
import com.project.raif.models.dto.subscription.SubscriptionDto;
import com.project.raif.models.dto.subscription.SubscriptionInfoResponse;
import com.project.raif.models.entity.Fund;
import com.project.raif.models.entity.Qr;
import com.project.raif.models.enums.CurrencyType;
import com.project.raif.models.enums.QrStatus;
import com.project.raif.models.enums.QrType;
import com.project.raif.models.enums.SubscriptionStatus;
import com.project.raif.repositories.FundRepository;
import com.project.raif.repositories.QrRepository;
import com.project.raif.services.clients.RaifQrClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class QrService {
    private final QrRepository qrRepository;
    private final FundRepository fundRepository;
    private final RaifQrClient qrClient;

    public QrResponse getQr(QrFrontRequest frontRequest, String fundUsername) {
        // preprocessing
        QrApiRequest apiRequest = QrApiRequest.builder()
                .amount(frontRequest.getAmount())
                .sbpMerchantId(frontRequest.getSbpMerchantId())
                .qrExpirationDate(ZonedDateTime.now().plusHours(1))
                .order(UUID.randomUUID().toString())
                .currency(CurrencyType.RUB)
                .qrType(QrType.QRDynamic)
                .build();
        if (frontRequest.getSubscription() != null) {
            apiRequest.setSubscription(new SubscriptionDto(UUID.randomUUID().toString(),
                    frontRequest.getSubscription().getSubscriptionPurpose()));
        }
        // Raif processing
        QrResponse apiResponse = qrClient.getQr(apiRequest);

        // internal processing and saving
        Qr qrEntity = new Qr(frontRequest.getSbpMerchantId(), frontRequest.getAmount(),
                apiRequest.getQrExpirationDate(), apiResponse);

        // if it's a simple qr registration
        // then apiResponse won't contain subscriptionId field
        if (frontRequest.getSubscription() != null) {
            qrEntity.setSubscriptionId(apiResponse.getSubscriptionId());
            qrEntity.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
        }
        Fund fund = fundRepository.findByLogin(fundUsername).orElseThrow(() ->
                new ApiException(ErrorCode.ERROR_NOT_FOUND_FUND, ErrorCode.ERROR_NOT_FOUND_FUND.getMessage()));
        fund.addQr(qrEntity);
        qrEntity.assignFund(fund);

        try {
            fundRepository.save(fund);
            qrRepository.save(qrEntity);
        } catch (Exception ex) {
            log.error("Got exception while saving qr", ex);
        }
        return apiResponse;
    }

    public SubscriptionInfoResponse getSubscriptionQr(QrSubscriptionRequest frontRequest, String fundUsername) {
        // preprocessing
        QrSubscriptionRequest apiRequest = QrSubscriptionRequest.builder()
                .sbpMerchantId(frontRequest.getSbpMerchantId())
                .subscriptionPurpose(frontRequest.getSubscriptionPurpose())
                .id(UUID.randomUUID().toString())
                .build();

        // Raif processing
        SubscriptionInfoResponse apiResponse = qrClient.getSubscriptionQr(apiRequest);

        // internal processing and saving
        Qr qrEntity = new Qr(frontRequest.getSbpMerchantId(), apiResponse);

        Fund fund = fundRepository.findByLogin(fundUsername).orElseThrow(() ->
                new ApiException(ErrorCode.ERROR_NOT_FOUND_FUND, ErrorCode.ERROR_NOT_FOUND_FUND.getMessage()));
        fund.addQr(qrEntity);
        qrEntity.assignFund(fund);

        try {
            fundRepository.save(fund);
            qrRepository.save(qrEntity);
        } catch (Exception ex) {
            log.error("Got exception while saving qr", ex);
        }
        return apiResponse;
    }

    public String getPaymentInfo(String qrId) {
        List<Qr> qrs = qrRepository.findByQrId(qrId);
        Qr qr = qrs.get(0);
        return qr.getQrStatus().toString();
    }

    public String getSubscriptionStatus(String subscriptionId) {
        List<Qr> qrs = qrRepository.findBySubscriptionId(subscriptionId);
        Qr qr = qrs.get(0);
        return qr.getSubscriptionStatus().toString();
    }
}
