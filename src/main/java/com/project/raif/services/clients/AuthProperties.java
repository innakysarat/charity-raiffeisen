package com.project.raif.services.clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class AuthProperties {
    private String secretKey;
    private String tokenPrefix;
}