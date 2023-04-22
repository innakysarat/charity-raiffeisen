package com.project.raif.controllers;

import com.project.raif.models.dto.qr.QrFrontRequest;
import com.project.raif.models.dto.qr.QrResponse;
import com.project.raif.models.dto.subscription.SubscriptionInfoResponse;
import com.project.raif.models.dto.subscription.SubscriptionPaymentRequest;
import com.project.raif.models.dto.subscription.SubscriptionPaymentResponse;
import com.project.raif.services.QrService;
import com.project.raif.services.clients.RaifQrClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/subscriptions")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SubscriptionController {
    private final RaifQrClient raifClient;
    private final QrService qrService;

    @PostMapping("/register")
    public QrResponse getSubscriptionQr(@RequestBody QrFrontRequest frontRequest) {
        log.info("Get subscription request, sbpMerchantId={}.", frontRequest.getSbpMerchantId());
        return qrService.registerQr(frontRequest);
    }

    @GetMapping("/getSubscriptionInfo/{subscriptionId}")
    public SubscriptionInfoResponse getSubscriptionInfo(@PathVariable String subscriptionId) {
        log.info("Get subscription info request, subscriptionId={}.", subscriptionId);

        return raifClient.getSubscriptionInfo(subscriptionId);
    }

    //mock
    @PostMapping("/payment/{subscriptionId}")
    public SubscriptionPaymentResponse payment(@PathVariable String subscriptionId, @RequestBody SubscriptionPaymentRequest request) {
        log.info("Subscription payment request, subscriptionId={}.", subscriptionId);

        return raifClient.submitPayment(subscriptionId, request);
    }

    @GetMapping("/payment/{subscriptionId}/{order}")
    public SubscriptionPaymentResponse getStatus(@PathVariable String subscriptionId, @PathVariable String order) {
        log.info("Payment status request, subscriptionId={}, order={}.", subscriptionId, order);

        return raifClient.checkStatus(subscriptionId, order);
    }
}
