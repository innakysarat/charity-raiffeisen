package com.project.raif.controllers;

import com.project.raif.models.dto.DateRangeDto;
import com.project.raif.models.entity.Fund;
import com.project.raif.models.entity.Qr;
import com.project.raif.services.FundService;
import io.swagger.annotations.Api;
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
@Api(description = "fund")
public class FundController {
    private final FundService fundService;

    @PostMapping
    public Fund create(@RequestBody Fund fund) {
        return fundService.create(fund);
    }

    @DeleteMapping("/{fundId}")
    public void delete(@PathVariable Long fundId) {
        String fundUsername = authentication();
        fundService.delete(fundId, fundUsername);
    }

    @GetMapping("/qrs")
    public List<Qr> getQrs() {
        String fundUsername = authentication();
        return fundService.getQrs(fundUsername);
    }

    @GetMapping("/income")
    public TreeMap<LocalDate, BigDecimal> getIncome() {
        String fundUsername = authentication();
        return fundService.getIncome(fundUsername, null);

    }

    @GetMapping("/average-cheque")
    public TreeMap<LocalDate, BigDecimal> getAverageCheque() {
        String fundUsername = authentication();
        return fundService.getAvgCheque(fundUsername, null);

    }

    @GetMapping("/transaction/count")
    public TreeMap<LocalDate, Long> getTotalCount() {
        String fundUsername = authentication();
        return fundService.getCntTransactions(fundUsername, null);

    }

    @GetMapping("/subscription/count")
    public TreeMap<LocalDate, Long> getSubscriptionTotalCount() {
        String fundUsername = authentication();
        return fundService.getCntSubscriptions(fundUsername, null);

    }

    @GetMapping("/subscription/average-cheque")
    public TreeMap<LocalDate, BigDecimal> getSubscriptionAvgCheque() {
        String fundUsername = authentication();
        return fundService.getSubscriptionAvgCheque(fundUsername, null);

    }

    @GetMapping("/statistics/{date}")
    public Map<String, BigDecimal> getOneDayStatistics(@PathVariable String date) {
        String fundUsername = authentication();
        return fundService.getOneDayStatistics(fundUsername, date);
    }

    @PostMapping("/statistics/date-range")
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
