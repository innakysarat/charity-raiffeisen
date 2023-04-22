package com.project.raif.models.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPaymentResponse {
    private BigDecimal amount;
    private String currency;
    private String order;
    private String paymentStatus;
    private String qrId;
    private String sbpMerchantId;
}