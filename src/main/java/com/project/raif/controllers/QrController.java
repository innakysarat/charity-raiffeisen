package com.project.raif.controllers;

import com.project.raif.models.dto.qr.QrFrontRequest;
import com.project.raif.models.dto.qr.QrResponse;
import com.project.raif.services.QrService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/qrs")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class QrController {
    private final QrService qrService;

    @PostMapping("/getQr")
    public QrResponse registerQr(@RequestBody QrFrontRequest qrRequestFromFront) {
        log.info("Qr request: /qrs/getQr");

        return qrService.registerQr(qrRequestFromFront);
    }
    @GetMapping("/info/{qrId}")
    public String getPaymentStatus(@PathVariable Long qrId) {
        log.info("Qr id: {}", qrId);
        return qrService.getPaymentInfo(qrId);
    }
}
