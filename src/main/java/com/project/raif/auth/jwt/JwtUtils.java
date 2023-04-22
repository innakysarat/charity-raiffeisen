package com.project.raif.auth.jwt;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setTitle(claims.get("title", String.class));
        jwtInfoToken.setLogin(claims.getSubject());

        return jwtInfoToken;
    }
}
