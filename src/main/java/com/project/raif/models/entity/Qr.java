package com.project.raif.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.raif.models.dto.qr.QrResponse;
import com.project.raif.models.dto.subscription.SubscriptionInfoResponse;
import com.project.raif.models.enums.QrStatus;
import com.project.raif.models.enums.SubscriptionStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Qr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public QrStatus qrStatus;
    public SubscriptionStatus subscriptionStatus;
    private String qrId;
    private String subscriptionId;
    private String sbpMerchantId;
    private BigDecimal amount;
    private ZonedDateTime qrExpirationDate;
    private String payload;
    private String qrUrl;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fund_id")
    private Fund fund;

    public Qr(String sbpMerchantId,
              BigDecimal amount,
              ZonedDateTime qrExpirationDate,
              QrResponse qrResponse) {
        this.sbpMerchantId = sbpMerchantId;
        this.amount = amount;
        this.qrExpirationDate = qrExpirationDate;
        this.qrId = qrResponse.qrId;
        this.qrStatus = qrResponse.qrStatus;
        this.payload = qrResponse.payload;
        this.qrUrl = qrResponse.qrUrl;
        this.subscriptionId = qrResponse.subscriptionId;
    }

    public Qr(String sbpMerchantId,
              SubscriptionInfoResponse qrResponse) {
        this.sbpMerchantId = sbpMerchantId;
        this.subscriptionId = qrResponse.getId();
        this.subscriptionStatus = qrResponse.getStatus();
        this.qrId = qrResponse.getQrDto().id;
        this.qrUrl = qrResponse.getQrDto().url;
        this.payload = qrResponse.getQrDto().payload;
    }

    public void assignFund(Fund fund) {
        this.fund = fund;
    }
}
