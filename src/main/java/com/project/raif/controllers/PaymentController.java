package com.project.raif.controllers;

import com.project.raif.models.dto.subscription.PaymentSubscriptionRequest;
import com.project.raif.models.dto.subscription.PaymentSubscriptionResponse;
import com.project.raif.services.QrService;
import com.project.raif.services.ReceiptService;
import com.project.raif.services.clients.RaifQrClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/payments")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {
    private final QrService qrService;
    private final ReceiptService receiptService;
    private final RaifQrClient raifClient;

    @PostMapping("/subscription/{subscriptionId}")
    public PaymentSubscriptionResponse payment(@PathVariable String subscriptionId,
                                               @RequestBody PaymentSubscriptionRequest request) {
        log.info("Subscription payment request, subscriptionId={}.", subscriptionId);

        return raifClient.submitPayment(subscriptionId, request);
    }

    @GetMapping("/info/{qrId}")
    public String getPaymentStatus(@PathVariable String qrId) {
        log.info("Qr id: {}", qrId);
        return qrService.getPaymentInfo(qrId);
    }

    @GetMapping("/subscription/{subscriptionId}/{order}")
    public PaymentSubscriptionResponse getSubscriptionPaymentStatus(@PathVariable String subscriptionId,
                                                                    @PathVariable String order) {
        log.info("Payment status request, subscriptionId={}, order={}.", subscriptionId, order);

        return raifClient.checkStatus(subscriptionId, order);
    }

    @GetMapping("/getReceipt/{qrId}")
    public String getReceiptNumber(@PathVariable String qrId) {
        return receiptService.getReceipt(qrId);
    }
}
