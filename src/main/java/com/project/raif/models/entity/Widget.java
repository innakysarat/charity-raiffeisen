package com.project.raif.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Widget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    String sbpMerchantId;
    String templateId;
    String templateProps;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fund_id")
    private Fund fund;

    public Widget(String sbpMerchantId, String templateId, String templateProps) {
        this.sbpMerchantId = sbpMerchantId;
        this.templateId = templateId;
        this.templateProps = templateProps;
    }

    public void assignFund(Fund fund) {
        this.fund = fund;
    }
}
