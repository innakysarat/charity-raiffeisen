package com.project.raif.models.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    private String id;
    private String subscriptionPurpose;
}