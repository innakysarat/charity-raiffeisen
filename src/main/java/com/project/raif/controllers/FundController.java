package com.project.raif.controllers;

import com.project.raif.models.dto.DateRangeDto;
import com.project.raif.models.entity.Fund;
import com.project.raif.models.entity.Qr;
import com.project.raif.services.FundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Slf4j
@RestController
@RequestMapping(value = "/fund")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FundController {
    private final FundService fundService;

    // fundDto
    @PostMapping
    @Operation(summary = "Регистрация фонда в системе")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Фонд зарегистрирован в системе")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public Fund create(@RequestBody Fund fund) {
        return fundService.create(fund);
    }

    @DeleteMapping("/{fundId}")
    @Operation(summary = "Удаление фонда из системы")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Фонд был удален")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public void delete(@PathVariable Long fundId) {
        String fundUsername = authentication();
        fundService.delete(fundId, fundUsername);
    }

    @GetMapping("/qrs")
    @Operation(summary = "Получение созданных фондом QR")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Список QR получен")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public List<Qr> getQrs() {
        String fundUsername = authentication();
        return fundService.getQrs(fundUsername);
    }

    @GetMapping("/income")
    @Operation(summary = "Получение выручки фонда")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Значения выручки получены")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public TreeMap<LocalDate, BigDecimal> getIncome() {
        String fundUsername = authentication();
        return fundService.getIncome(fundUsername, null);

    }

    @GetMapping("/lost-income")
    @Operation(summary = "Получение упущенной выгоды фонда")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Значения упущенной выгоды получены")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public TreeMap<LocalDate, BigDecimal> getLostIncome() {
        String fundUsername = authentication();
        return fundService.getLostIncome(fundUsername, null);

    }

    @GetMapping("/average-cheque")
    @Operation(summary = "Получение среднего чека платежей")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Значения среднего чека получены")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public TreeMap<LocalDate, BigDecimal> getAverageCheque() {
        String fundUsername = authentication();
        return fundService.getAvgCheque(fundUsername, null);

    }

    @GetMapping("/transaction/count")
    @Operation(summary = "Получение метрики количество плажетей")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Значения метрики количество платежей получены")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public TreeMap<LocalDate, Long> getTransactionCount() {
        String fundUsername = authentication();
        return fundService.getTransactionCount(fundUsername, null);

    }

    @GetMapping("/subscription/count")
    @Operation(summary = "Получение метрики количества подписок")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Значения метрики количества подписок получены")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public TreeMap<LocalDate, Long> getSubscriptionCount() {
        String fundUsername = authentication();
        return fundService.getSubscriptionCount(fundUsername, null);

    }

    @GetMapping("/subscription/average-cheque")
    @Operation(summary = "Получение среднего чека подписок")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Значения среднего чека подписок получены")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public TreeMap<LocalDate, BigDecimal> getSubscriptionAvgCheque() {
        String fundUsername = authentication();
        return fundService.getSubscriptionAvgCheque(fundUsername, null);

    }

    @GetMapping("/statistics/{date}")
    @Operation(summary = "Получение значений метрик за указанный день")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Значения метрик за указанный день получены")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public Map<String, BigDecimal> getOneDayStatistics(@PathVariable String date) {
        String fundUsername = authentication();
        return fundService.getOneDayStatistics(fundUsername, date);
    }

    @PostMapping("/statistics/date-range")
    @Operation(summary = "Получение значений метрик за указанный промежуток времени")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Значения метрик за указанный промежуток времени получены")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public List<TreeMap<LocalDate, BigDecimal>> getStatisticsByDateRange(@RequestBody DateRangeDto dto) {
        String fundUsername = authentication();
        return fundService.getStatisticsByDateRange(fundUsername, dto.getStartDate(), dto.getEndDate());
    }

    private String authentication() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String fundUsername = authentication == null ? null : (String) authentication.getPrincipal();
        if (Objects.equals(fundUsername, "anonymousUser")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No access to resource"
            );
        }
        return fundUsername;
    }
}
