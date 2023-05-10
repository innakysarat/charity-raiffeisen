package com.project.raif.models.dto;

import com.project.raif.models.enums.VatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String name;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal amount;
    private VatType vatType;
}
