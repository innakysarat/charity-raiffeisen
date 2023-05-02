package com.project.raif.models.dto.qr;

import com.project.raif.models.dto.subscription.SubscriptionDto;
import com.project.raif.models.enums.QrType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class QrFrontRequest {
    private String sbpMerchantId;
    private QrType qrType;
    private BigDecimal amount;
    private String currency;
    private String additionalInfo;
    private SubscriptionDto subscription;
}
