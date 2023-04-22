package com.project.raif.services.clients;

import com.project.raif.models.dto.qr.QrApiRequest;
import com.project.raif.models.dto.qr.QrPaymentResponse;
import com.project.raif.models.dto.qr.QrResponse;
import com.project.raif.models.dto.subscription.SubscriptionInfoResponse;
import com.project.raif.models.dto.subscription.SubscriptionPaymentRequest;
import com.project.raif.models.dto.subscription.SubscriptionPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RaifQrClient {
    private final static String baseUrl = "https://pay-test.raif.ru/api";
    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthConfig authConfig;

    public RaifQrClient(AuthConfig authConfig) {
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

    public SubscriptionPaymentResponse submitPayment(String subscriptionId, SubscriptionPaymentRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authConfig.secretKey());

        String url = String.format("%s/sbp/v1/subscriptions/%s/orders", baseUrl, subscriptionId);

        try {
            ResponseEntity<SubscriptionPaymentResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(request, headers), SubscriptionPaymentResponse.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client", ex);
            return null;
        }
    }

    public SubscriptionPaymentResponse checkStatus(String subscriptionId, String order) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authConfig.secretKey());

        String url = String.format("%s/sbp/v1/subscriptions/%s/orders/%s", baseUrl, subscriptionId, order);

        try {
            ResponseEntity<SubscriptionPaymentResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(null, headers), SubscriptionPaymentResponse.class);

            return response.getBody();
        } catch (Exception ex) {
            log.error("Got exception from raif client", ex);
            return null;
        }
    }
}