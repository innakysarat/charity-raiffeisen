package com.project.raif.controllers;

import com.project.raif.services.AuthService;
import com.project.raif.auth.Generator;
import com.project.raif.auth.RefreshRequest;
import com.project.raif.auth.jwt.JwtRequest;
import com.project.raif.auth.jwt.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.security.auth.message.AuthException;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/access")
    @Operation(summary = "Получение access токена")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Access токен получен")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public JwtResponse getAccessToken(@RequestBody RefreshRequest refreshRequest) {
        return authService.getAccessToken(refreshRequest);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Получение refresh токена")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Refresh токен получен")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public JwtResponse getRefreshToken(@RequestBody RefreshRequest refreshRequest) throws AuthException {
        return authService.getRefreshToken(refreshRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизация в личный кабинет")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Фонд успешно авторизован в системе")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public JwtResponse login(@RequestBody JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }

    @GetMapping("/generate")
    @Operation(summary = "Генерация ключа")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Ключ сгенерирован")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public String generateKey() {
        return Generator.generateKey();
    }
}
