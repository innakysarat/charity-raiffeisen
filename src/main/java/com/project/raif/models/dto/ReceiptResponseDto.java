package com.project.raif.models.dto;

import com.project.raif.models.entity.Client;
import com.project.raif.models.enums.ReceiptStatus;
import com.project.raif.models.enums.ReceiptType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponseDto {
    private String receiptNumber;
    private ReceiptType receiptType;
    private ReceiptStatus status;
    private Client client;
    private List<Item> items;
    private BigDecimal total;
}
