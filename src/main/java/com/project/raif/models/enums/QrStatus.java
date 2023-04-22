package com.project.raif.models.enums;

public enum QrStatus {
    INACTIVE("QR неактивен"),
    NEW("QR готов к оплате"),
    IN_PROGRESS("QR в процессе обработки"),
    PAID("QR оплачен"),
    EXPIRED("QR истёк"),
    CANCELLED("QR отменён");

    private final String description;

    QrStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}