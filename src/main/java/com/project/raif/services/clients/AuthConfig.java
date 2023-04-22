package com.project.raif.services.clients;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AuthProperties.class})
@AllArgsConstructor
public class AuthConfig {

    private final AuthProperties authProperties;

    @Bean
    public String secretKey() {
        return authProperties.getSecretKey();
    }
}