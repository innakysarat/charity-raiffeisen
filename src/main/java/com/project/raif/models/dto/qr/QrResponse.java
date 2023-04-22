package com.project.raif.models.dto.qr;

import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QrResponse {
    public String qrId;
    public String qrStatus;
    public String payload;
    public String qrUrl;
    public String subscriptionId;
}