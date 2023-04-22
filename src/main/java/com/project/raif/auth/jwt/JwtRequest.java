package com.project.raif.auth.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequest {
    private String login;
    private String password;
}
