package com.project.raif.models.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPaymentRequest {
    private String additionalInfo;
    private long amount;
    private String currency;
}