package com.project.raif.models.dto.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.raif.models.enums.SubscriptionStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionInfoResponse {

    @ApiModelProperty("subscription_id")
    private String id;
    private String bank;
    private String createDate;
    private SubscriptionStatus status;
    private QrDto qrDto;

    @SuppressWarnings("unchecked")
    @JsonProperty("qr")
    private void unpackNested(Map<String, Object> qr) {
        String qrId = (String) qr.get("id");
        String payload = (String) qr.get("payload");
        String url = (String) qr.get("url");
        this.qrDto = new QrDto(qrId, payload, url);
    }
}