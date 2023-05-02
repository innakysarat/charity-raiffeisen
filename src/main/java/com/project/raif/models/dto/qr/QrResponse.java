package com.project.raif.models.dto.qr;

import com.project.raif.models.enums.QrStatus;
import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QrResponse {
    public String qrId;
    public QrStatus qrStatus;
    public String payload;
    public String qrUrl;
    public String subscriptionId;
}