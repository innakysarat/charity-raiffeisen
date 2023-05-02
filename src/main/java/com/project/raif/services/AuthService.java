package com.project.raif.services;

import com.project.raif.auth.PasswordConfig;
import com.project.raif.auth.RefreshRequest;
import com.project.raif.auth.jwt.JwtProvider;
import com.project.raif.auth.jwt.JwtRequest;
import com.project.raif.auth.jwt.JwtResponse;
import com.project.raif.models.entity.Fund;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final FundService fundService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    PasswordConfig passwordConfig;

    public JwtResponse getAccessToken(RefreshRequest refreshRequest) {
        if (jwtProvider.validateRefreshToken(refreshRequest.getRefreshToken())) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshRequest.getRefreshToken());
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshRequest.getRefreshToken())) {
                final Fund fund = fundService.getFund(login);
                final String accessToken = jwtProvider.generateAccessToken(fund);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse getRefreshToken(RefreshRequest refreshRequest) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshRequest.getRefreshToken())) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshRequest.getRefreshToken());
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshRequest.getRefreshToken())) {
                final Fund fund = fundService.getFund(login);
                final String accessToken = jwtProvider.generateAccessToken(fund);
                final String newRefreshToken = jwtProvider.generateRefreshToken(fund);

                refreshStorage.put(fund.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен.");
    }

    public JwtResponse login(JwtRequest jwtRequest) {
        try {
            Fund fund = fundService.getFund(jwtRequest.getLogin());
            if (passwordConfig.passwordEncoder().matches(jwtRequest.getPassword(), fund.getPassword())) {
                String accessToken = jwtProvider.generateAccessToken(fund);
                String refreshToken = jwtProvider.generateRefreshToken(fund);
                refreshStorage.put(fund.getLogin(), refreshToken);

                return new JwtResponse(accessToken, refreshToken);
            } else {
                throw new IllegalArgumentException("Невалидный пароль.");
            }
        } catch (NullPointerException ex) {
            return null;
        }
    }
}
