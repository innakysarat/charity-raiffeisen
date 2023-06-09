package com.project.raif.models.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSubscriptionRequest {
    private String additionalInfo;
    private BigDecimal amount;
    private String currency;
}