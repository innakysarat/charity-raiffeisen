package com.project.raif.models.enums;

public enum VatType {
    // без НДС
    NONE,
    // НДС по ставке 0%
    VAT0,
    // НДС чека по ставке 10%
    VAT10,
    // НДС чека по расчетной ставке 10/110
    VAT110,
    // НДС чека по ставке 20%
    VAT20,
    // НДС чека по расчетной ставке 20/120
    VAT120
}
