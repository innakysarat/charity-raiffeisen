package com.project.raif.controllers;

import com.project.raif.models.dto.qr.QrFrontRequest;
import com.project.raif.models.dto.qr.QrResponse;
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
@RequestMapping(value = "/qrs")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class QrController {
    private final QrService qrService;

    @PostMapping("/getQr")
    @Operation(summary = "Генерация QR для разового платежа")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. QR для разового платежа был создан")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public QrResponse getQr(@RequestBody QrFrontRequest frontRequest) {
        log.info("Qr request: /qrs/getQr");

        String fundUsername = authentication();
        return qrService.getQr(frontRequest, fundUsername);
    }

    @PostMapping("/getSubscriptionQr")
    @Operation(summary = "Генерация QR для платежа с подпиской на ежемесячные списания")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. QR для платежа с подпиской был создан")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public QrResponse getPaymentSubscriptionQr(@RequestBody QrFrontRequest frontRequest) {
        log.info("Get subscription request, sbpMerchantId={}.", frontRequest.getSbpMerchantId());

        String fundUsername = authentication();
        return qrService.getQr(frontRequest, fundUsername);
    }

    private String authentication() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String fundUsername = authentication == null ? null : (String) authentication.getPrincipal();
        if (Objects.equals(fundUsername, "anonymousUser")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No access to generating QR"
            );
        }
        return fundUsername;
    }
}
