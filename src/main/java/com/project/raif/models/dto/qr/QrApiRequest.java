package com.project.raif.models.dto.qr;

import com.project.raif.models.dto.subscription.Subscription;
import com.project.raif.models.enums.CurrencyType;
import com.project.raif.models.enums.QrType;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QrApiRequest {
    private QrType qrType;
    private Long account;
    private String additionalInfo;
    private BigDecimal amount;
    private CurrencyType currency;
    private String order;
    private String paymentDetails;
    private ZonedDateTime qrExpirationDate;
    private String sbpMerchantId;
    private String redirectUrl;
    private Subscription subscription;
}