package com.project.raif.controllers;

import com.project.raif.models.dto.subscription.PaymentSubscriptionRequest;
import com.project.raif.models.dto.subscription.PaymentSubscriptionResponse;
import com.project.raif.services.QrService;
import com.project.raif.services.ReceiptService;
import com.project.raif.services.clients.RaifClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final RaifClientService raifClientService;

    @PostMapping("/subscription/{subscriptionId}")
    public PaymentSubscriptionResponse payment(@PathVariable String subscriptionId,
                                               @RequestBody PaymentSubscriptionRequest request) {
        log.info("Subscription payment request, subscriptionId={}.", subscriptionId);

        return raifClientService.submitPayment(subscriptionId, request);
    }

    @GetMapping("/info/{qrId}")
    @Operation(summary = "Получение статуса платежа")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Статус платежа получен")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public String getPaymentStatus(@PathVariable String qrId) {
        log.info("Qr id: {}", qrId);
        return qrService.getPaymentInfo(qrId);
    }

    @GetMapping("/subscription/{subscriptionId}/{order}")
    @Operation(summary = "Получение статуса подписки")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Статус подписки получен")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public PaymentSubscriptionResponse getSubscriptionPaymentStatus(@PathVariable String subscriptionId,
                                                                    @PathVariable String order) {
        log.info("Payment status request, subscriptionId={}, order={}.", subscriptionId, order);

        return raifClientService.checkStatus(subscriptionId, order);
    }

    @GetMapping("/getReceipt/{qrId}")
    @Operation(summary = "Получение номера фискального чека")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Номер фискального чека получен")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public String getReceiptNumber(@PathVariable String qrId) {
        return receiptService.getReceipt(qrId);
    }
}
