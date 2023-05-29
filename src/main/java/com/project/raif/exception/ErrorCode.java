package com.project.raif.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    ERROR_QR_NOT_FOUND("ERROR.QR_NOT_FOUND", "По данному id нет QR"),
    ERROR_PAYMENT_NOT_FOUND("ERROR.PAYMENT_NOT_FOUND", "По данному id нет платежа"),
    ERROR_QR_EXPIRED("ERROR.QR_EXPIRED", "Срок жизни QR истёк"),
    ERROR_FUND_NOT_FOUND("ERROR.NOT_FOUND_FUND", "По указанным параметрам фонда не найдено"),
    ERROR_NO_ACCESS_TO_RESOURCE("ERROR.NO_ACCESS_TO_RESOURCE", "Нет доступа к ресурсу"),
    ERROR_FUND_ALREADY_EXISTS("ERROR.FUND_ALREADY_EXISTS", "Фонд с такими параметрами уже существует"),
    ERROR_WIDGET_NOT_FOUND("ERROR.WIDGET_NOT_FOUND", "По данному id виджет не найден"),
    ERROR_INVALID_PASSWORD("ERROR.INVALID_PASSWORD", "Невалидный пароль при входе в приложение")
    ;

    private final String code;
    private final String message;
}
