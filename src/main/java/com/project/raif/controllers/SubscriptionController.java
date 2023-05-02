package com.project.raif.controllers;

import com.project.raif.models.dto.subscription.SubscriptionInfoResponse;
import com.project.raif.models.dto.subscription.QrSubscriptionRequest;
import com.project.raif.services.QrService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/subscriptions")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Api(description = "subscriptions")
public class SubscriptionController {
    private final QrService qrService;

    @PostMapping("/getQr")
    public SubscriptionInfoResponse getSubscriptionQr(@RequestBody QrSubscriptionRequest frontRequest) {
        log.info("Get subscription request, sbpMerchantId={}.", frontRequest.getSbpMerchantId());
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String fundUsername = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(fundUsername, "anonymousUser")) {
            return qrService.getSubscriptionQr(frontRequest, fundUsername);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No access to generating QR"
            );
        }
    }
    @GetMapping("/info/{subscriptionId}")
    public String getSubscriptionInfo(@PathVariable String subscriptionId) {
        log.info("Get subscription info request, subscriptionId={}.", subscriptionId);

        return qrService.getSubscriptionStatus(subscriptionId);
    }
}
