package com.project.raif.controllers;

import com.project.raif.services.AuthService;
import com.project.raif.auth.Generator;
import com.project.raif.auth.RefreshRequest;
import com.project.raif.auth.jwt.JwtRequest;
import com.project.raif.auth.jwt.JwtResponse;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Api(description = "auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/access")
    public JwtResponse getAccessToken(@RequestBody RefreshRequest refreshRequest) {
        return authService.getAccessToken(refreshRequest);
    }

    @PostMapping("/refresh")
    public JwtResponse getRefreshToken(@RequestBody RefreshRequest refreshRequest) throws AuthException {
        return authService.getRefreshToken(refreshRequest);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }

    @GetMapping("/generate")
    public String generateKey() {
        return Generator.generateKey();
    }
}
