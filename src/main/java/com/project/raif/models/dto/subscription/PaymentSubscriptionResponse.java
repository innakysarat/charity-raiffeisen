package com.project.raif.models.dto.subscription;

import com.project.raif.models.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSubscriptionResponse {
    private BigDecimal amount;
    private String currency;
    private String order;
    private PaymentStatus paymentStatus;
    private String qrId;
    private String sbpMerchantId;
}