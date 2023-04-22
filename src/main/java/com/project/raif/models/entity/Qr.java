package com.project.raif.models.entity;

import com.project.raif.models.dto.qr.QrResponse;
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
    private String qrId;
    private String subscriptionId;
    private String sbpMerchantId;
    private BigDecimal amount;
    public String qrStatus;
    private ZonedDateTime qrExpirationDate;
    private String payload;
    private String qrUrl;

    public Qr(String sbpMerchantId,
              BigDecimal amount,
              ZonedDateTime qrExpirationDate,
              QrResponse qrResponse) {
        this.sbpMerchantId = sbpMerchantId;
        this.amount = amount;
        this.qrId = qrResponse.qrId;
        this.qrStatus = qrResponse.qrStatus;
        this.payload = qrResponse.payload;
        this.qrUrl = qrResponse.qrUrl;
        this.subscriptionId = qrResponse.subscriptionId;
        this.qrExpirationDate = qrExpirationDate;
    }
}
