package com.project.raif.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
@AllArgsConstructor
@Getter
public class WidgetRequestDto {
    String templateId;
    String templateProps;
}
