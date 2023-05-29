package com.project.raif.auth.jwt;

import com.project.raif.models.entity.Fund;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    private final SecretKey accessSecret;
    private final SecretKey refreshSecret;

    public JwtProvider(@Value("${jwt.secret.access}") String accessSecret,
                       @Value("${jwt.secret.refresh}") String refreshSecret) {
        this.accessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
    }

    public String generateAccessToken(@NonNull Fund fund) {
        final LocalDateTime now = LocalDateTime.now();
        // !!! должно быть полчаса
        final Instant accessExpiration = now.plusHours(3).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpirationDate = Date.from(accessExpiration);
        return Jwts.builder()
                .setSubject(fund.getLogin())
                .setExpiration(accessExpirationDate)
                .signWith(accessSecret)
                .claim("title", fund.getTitle())
                .compact();
    }

    public String generateRefreshToken(@NonNull Fund fund) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpiration = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpirationDate = Date.from(refreshExpiration);
        return Jwts.builder()
                .setSubject(fund.getLogin())
                .setExpiration(refreshExpirationDate)
                .signWith(refreshSecret)
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, accessSecret);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, refreshSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull SecretKey secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    private Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, accessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, refreshSecret);
    }
}
