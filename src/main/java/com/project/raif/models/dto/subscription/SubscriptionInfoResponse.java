package com.project.raif.models.dto.subscription;

import com.project.raif.models.entity.Qr;
import com.project.raif.models.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionInfoResponse {

    private String id;
    private String bank;
    private String createDate;
    private SubscriptionStatus status;
    private Qr subscriptionQr;
}