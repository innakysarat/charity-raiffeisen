package com.project.raif.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    ERROR_NOT_FOUND_QR("ERROR_NOT_FOUND_QR", "По данному id нет QR"),
    ERROR_NOT_FOUND_PAYMENT("ERROR_NOT_FOUND_PAYMENT", "По данному id нет платежа"),
    ERROR_QR_EXPIRED("ERROR_QR_EXPIRED", "Срок жизни QR истёк"),
    ERROR_NOT_FOUND_FUND("ERROR_NOT_FOUND_FUND", "По данному id фонда не найдено");

    private final String code;
    private final String message;
}
