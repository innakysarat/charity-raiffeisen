package com.project.raif.models.dto.qr;

import com.project.raif.models.enums.PaymentStatus;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
@Data
@Getter
public class QrPaymentResponse {
    private String additionalInfo;
    private String paymentPurpose;
    private BigDecimal amount;
    private String code;
    private String createDate;
    private String currency;
    private Long merchantId;
    private String order;
    private PaymentStatus paymentStatus;
    private String qrId;
    private String spbMerchantId;
    private String transactionDate;
    private Integer transactionId;
    private String qrExpirationDate;
}