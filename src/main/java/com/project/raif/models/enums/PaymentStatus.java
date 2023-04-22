package com.project.raif.models.enums;

public enum PaymentStatus {
    SUCCESS("Платёж прошёл успешно"),
    DECLINED("Платёж отклонён"),
    NO_INFO("Не найдена информация о платеже"),
    IN_PROGRESS("Платёж в процессе обработки");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}