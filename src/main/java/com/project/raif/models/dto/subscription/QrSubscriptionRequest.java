package com.project.raif.models.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrSubscriptionRequest {
    private String id;
    private String subscriptionPurpose;
    private String sbpMerchantId;
    private String redirectUrl;
}
