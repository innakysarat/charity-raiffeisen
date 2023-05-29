package com.project.raif.controllers;

import com.project.raif.email.EmailDetails;
import com.project.raif.models.dto.ReceiptFrontRequest;
import com.project.raif.services.EmailService;
import com.project.raif.services.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/email")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class EmailController {
    private EmailService emailService;
    private ReceiptService receiptService;

    // две кнопки на фронте:
    // 1) отправить чек (клиентская почта)
    // 2) не отправлять чек (дефолтная почта)
    // при нажатии на любую из кнопок передаём qrId -> идёт предобработка
    // по айди восстанавливаем данные о сумме, названии фонда + нужна ли фискализация (благотворительность или нет)
    // если не нужна фискализация + кнопка "не отправлять" => ничего не делаем
    // если не нужна фискализация + кнопка "отправить" => отправляем сообщение на почту об успешной оплате
    // если нужна фискализация + кнопка "не отправлять" => создаём чек на дефолтную почту и регистрируем
    // если нужна фискализация + кнопка "отправить" => создаём чек на указанную почту и регистрируем

    // ручка, если на фронте нажата кнопка "отправить" и указана почта
    @PostMapping("/send")
    @Operation(summary = "Отправка чека о совершении платежа на почту клиента")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Письмо отправлено на почту клиента")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public String sendMail(@RequestBody ReceiptFrontRequest request) {
        // фискализация нужна + отправка на авторскую почту
        if (!receiptService.preprocessingQr(request.getQrId())) {
            EmailDetails emailDetails = EmailDetails.builder()
                    .qrId(request.getQrId())
                    .recipient(request.getEmail())
                    .msgBody("Фискальные данные")
                    .subject("Фискальный чек")
                    .build();
            return emailService.sendSimpleMail(emailDetails, true);
        }
        // фискализация не нужна + отправка на авторскую почту
        EmailDetails emailDetails = receiptService.createCharityReceipt(request.getEmail(), request.getQrId());
        return emailService.sendSimpleMail(emailDetails, false);
    }
    // ручка, если на фронте нажата кнопка "не отправлять" чек на почту
    @PostMapping("/sendDefault/{qrId}")
    @Operation(summary = "Отправка чека о совершении платежа на дефолтную почту")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Чек отправлен на дефолтную почту")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public String sendMailDefault(@PathVariable String qrId) {
        // фискализация нужна + отправка чека на дефолтную почту
        if (!receiptService.preprocessingQr(qrId)) {
            EmailDetails email = EmailDetails.builder()
                    .qrId(qrId)
                    .recipient("kysarat01@gmail.com")
                    .msgBody("Фискальные данные")
                    .subject("Фискальный чек")
                    .build();
            return emailService.sendSimpleMail(email, true);
        }
        // фискализации нет + не нужна отправка
        return "No email for donation";
    }

    @PostMapping("/sendWithAttachment")
    @Operation(summary = "Отправка письма с вложениями")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Письмо с вложениями было отправлено")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public String sendMailWithAttachment(@RequestBody EmailDetails details) {
        return emailService.sendMailWithAttachment(details);
    }
}