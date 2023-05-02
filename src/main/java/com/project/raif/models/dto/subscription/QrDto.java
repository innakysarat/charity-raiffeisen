package com.project.raif.models.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrDto {

    public String id;
    public String payload;
    public String url;
}
