package com.project.raif.services.clients;

import com.project.raif.models.dto.ReceiptRequestDto;
import com.project.raif.models.dto.ReceiptResponseDto;
import com.project.raif.models.dto.qr.QrApiRequest;
import com.project.raif.models.dto.qr.QrPaymentResponse;
import com.project.raif.models.dto.qr.QrResponse;
import com.project.raif.models.dto.subscription.PaymentSubscriptionRequest;
import com.project.raif.models.dto.subscription.PaymentSubscriptionResponse;
import com.project.raif.models.dto.subscription.QrSubscriptionRequest;
import com.project.raif.models.dto.subscription.SubscriptionInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RaifClientService {
    private final static String baseUrl = "https://pay-test.raif.ru/api";
    private final static String fiscalUrl = "https://test.ecom.raiffeisen.ru/api/fiscal/v1/receipts/sell";
    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthConfig authConfig;

    public RaifClientService(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    public QrResponse getQr(QrApiRequest qrRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = String.format("%s/sbp/v2/qrs", baseUrl);

        try {
            log.info("Sending qr request to raif: body: {}", qrRequest);

            ResponseEntity<QrResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(qrRequest, headers), QrResponse.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client", ex);
        }
        log.error("Error from raif client returning null");
        return null;
    }

    public SubscriptionInfoResponse getSubscriptionQr(QrSubscriptionRequest qrRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = String.format("%s/sbp/v1/subscriptions", baseUrl);

        try {
            log.info("Sending qr request to raif: body: {}", qrRequest);

            ResponseEntity<SubscriptionInfoResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(qrRequest, headers), SubscriptionInfoResponse.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client while creating subscription QR", ex);
        }
        log.error("Error from raif client returning null");
        return null;
    }


    public QrPaymentResponse getPaymentInfo(String qrId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authConfig.secretKey());

        String url = String.format("%s/sbp/v1/qr/%s/payment-info", baseUrl, qrId);

        try {
            log.info("Sending qrId to raif: {}", qrId);
            ResponseEntity<QrPaymentResponse> response = restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(null, headers), QrPaymentResponse.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client", ex);
        }
        log.error("Error from raif client returning null");
        return null;
    }

    public SubscriptionInfoResponse getSubscriptionInfo(String subscriptionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authConfig.secretKey());

        String url = String.format("%s/sbp/v1/subscriptions/%s", baseUrl, subscriptionId);

        try {
            ResponseEntity<SubscriptionInfoResponse> response = restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(null, headers), SubscriptionInfoResponse.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client", ex);
            return null;
        }
    }

    public PaymentSubscriptionResponse submitPayment(String subscriptionId, PaymentSubscriptionRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authConfig.secretKey());

        String url = String.format("%s/sbp/v1/subscriptions/%s/orders", baseUrl, subscriptionId);

        try {
            ResponseEntity<PaymentSubscriptionResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(request, headers), PaymentSubscriptionResponse.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client", ex);
            return null;
        }
    }

    public PaymentSubscriptionResponse checkStatus(String subscriptionId, String order) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authConfig.secretKey());

        String url = String.format("%s/sbp/v1/subscriptions/%s/orders/%s", baseUrl, subscriptionId, order);

        try {
            ResponseEntity<PaymentSubscriptionResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(null, headers), PaymentSubscriptionResponse.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client", ex);
            return null;
        }
    }

    public ReceiptResponseDto createReceipt(ReceiptRequestDto receiptRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authConfig.secretKey());

        try {
            ResponseEntity<ReceiptResponseDto> response = restTemplate.exchange(fiscalUrl, HttpMethod.POST,
                    new HttpEntity<>(receiptRequest, headers), ReceiptResponseDto.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client while a receipt creation", ex);
            return null;
        }
    }

    public ReceiptResponseDto registerReceipt(String receiptNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authConfig.secretKey());
        String url = String.format("%s/%s", fiscalUrl, receiptNumber);
        try {
            ResponseEntity<ReceiptResponseDto> response = restTemplate.exchange(url, HttpMethod.PUT,
                    new HttpEntity<>(null, headers), ReceiptResponseDto.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client while a receipt registration", ex);
            return null;
        }
    }

    public ReceiptResponseDto getReceiptStatus(String receiptNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authConfig.secretKey());
        String url = String.format("%s/%s", fiscalUrl, receiptNumber);
        try {
            ResponseEntity<ReceiptResponseDto> response = restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(null, headers), ReceiptResponseDto.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client while a receipt registration", ex);
            return null;
        }
    }
}