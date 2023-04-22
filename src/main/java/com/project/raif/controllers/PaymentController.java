package com.project.raif.controllers;

import com.project.raif.services.QrService;
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

    @GetMapping("/info/{qrId}")
    public String getPaymentStatus(@PathVariable Long qrId) {
        log.info("Qr id: {}", qrId);
        return qrService.getPaymentInfo(qrId);
    }
}
