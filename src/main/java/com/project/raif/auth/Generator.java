package com.project.raif.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

public class Generator {
    public static String generateKey() {
        return Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
    }
}