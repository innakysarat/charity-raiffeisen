package com.project.raif.controllers;

import com.project.raif.models.dto.subscription.SubscriptionInfoResponse;
import com.project.raif.models.dto.subscription.QrSubscriptionRequest;
import com.project.raif.services.QrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class SubscriptionController {
    private final QrService qrService;

    @PostMapping("/getQr")
    @Operation(summary = "Генерация QR для подписки на ежемесячные списания")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. QR для подписки на ежемесячные списания сгенерирован")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
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
    @Operation(summary = "Получение информации о подписке")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Информация по подписке получена")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public String getSubscriptionInfo(@PathVariable String subscriptionId) {
        log.info("Get subscription info request, subscriptionId={}.", subscriptionId);

        return qrService.getSubscriptionStatus(subscriptionId);
    }
}
