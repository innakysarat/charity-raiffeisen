package com.project.raif.controllers;

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

    @GetMapping("/profit")
    public Map<LocalDate, BigDecimal> getProfit() {
        String fundUsername = authentication();
        return fundService.getProfit(fundUsername);

    }

    @GetMapping("/avg-cheque")
    public Map<LocalDate, BigDecimal> getAverageCheque() {
        String fundUsername = authentication();
        return fundService.getAvgCheque(fundUsername);

    }

    @GetMapping("/total-cnt")
    public Map<LocalDate, Long> getTotalCount() {
        String fundUsername = authentication();
        return fundService.getCntTransactions(fundUsername);

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
