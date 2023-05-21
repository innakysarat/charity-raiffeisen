package com.project.raif.services;

import com.project.raif.auth.PasswordConfig;
import com.project.raif.exception.ApiException;
import com.project.raif.exception.ErrorCode;
import com.project.raif.models.entity.Fund;
import com.project.raif.models.entity.Qr;
import com.project.raif.models.enums.QrStatus;
import com.project.raif.repositories.FundRepository;
import com.project.raif.repositories.QrRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FundService {

    static final Map<String, TemporalAdjuster> ADJUSTERS = new HashMap<>();
    private final FundRepository fundRepository;
    private final QrRepository qrRepository;
    PasswordConfig passwordConfig;

    public static void trimDate() {
        ADJUSTERS.put("day", TemporalAdjusters.ofDateAdjuster(d -> d)); // identity
        ADJUSTERS.put("week", TemporalAdjusters.previousOrSame(DayOfWeek.of(1)));
        ADJUSTERS.put("month", TemporalAdjusters.firstDayOfMonth());
        ADJUSTERS.put("year", TemporalAdjusters.firstDayOfYear());
    }

    public List<Qr> preprocessingQrs(String fundUsername) {
        // find paid qrs
        List<Qr> paidQrs = qrRepository.findAllByQrStatus(QrStatus.PAID);
        List<Qr> notPaidQrs = qrRepository.findAllByQrStatus(QrStatus.CANCELLED);
        // find fund's paid qrs
        paidQrs.stream().filter(qr -> Objects.equals(qr.getFund().getLogin(), fundUsername))
                .collect(Collectors.toList());
        // find fund's unique paid qrs
        List<Qr> uniquePaidQrs = new ArrayList<>(paidQrs.stream()
                .collect(Collectors.toMap(Qr::getQrId, qr -> qr, (qr1, qr2) -> qr1))
                .values());
        trimDate();
        return uniquePaidQrs;
    }

    public Fund create(Fund fund) {
        Fund fundExists = fundRepository.findByLoginAndTitle(fund.getLogin(), fund.getTitle());
        if (fundExists != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Фонд с такими логином и названием уже зарегистрирован");
        }
        String encodedPassword = passwordConfig.passwordEncoder().encode(fund.getPassword());
        fund.setPassword(encodedPassword);
        return fundRepository.save(fund);
    }

    public Fund getFund(String login) {
        return fundRepository.findByLogin(login).orElseThrow(() ->
                new ApiException(ErrorCode.ERROR_NOT_FOUND_FUND, ErrorCode.ERROR_NOT_FOUND_FUND.getMessage()));
    }

    public void delete(Long fundId, String fundUsername) {
        Fund fund = fundRepository.findById(fundId)
                .orElseThrow(() ->
                        new ApiException(ErrorCode.ERROR_NOT_FOUND_FUND,
                                ErrorCode.ERROR_NOT_FOUND_FUND.getMessage()));
        if (Objects.equals(fund.getLogin(), fundUsername)) {
            fundRepository.deleteById(fundId);
        } else throw new ApiException(ErrorCode.ERROR_NO_ACCESS_TO_RESOURCE,
                ErrorCode.ERROR_NO_ACCESS_TO_RESOURCE.getMessage());
    }

    public List<Qr> getQrs(String fundUsername) {
        Fund fund = fundRepository.findByLogin(fundUsername).orElseThrow(() ->
                new ApiException(ErrorCode.ERROR_NOT_FOUND_FUND, ErrorCode.ERROR_NOT_FOUND_FUND.getMessage()));
        return qrRepository.findByFund(fund);
    }

    public Map<LocalDate, BigDecimal> getProfit(String fundUsername) {
        List<Qr> uniquePaidQrs = preprocessingQrs(fundUsername);
        return uniquePaidQrs.stream()
                .collect(Collectors.groupingBy(
                        qr -> qr.getQrPaymentDate()
                                .with(ADJUSTERS.get("day")),
                        Collectors.mapping(Qr::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }

    public Map<LocalDate, BigDecimal> getAvgCheque(String fundUsername) {
        List<Qr> uniquePaidQrs = preprocessingQrs(fundUsername);
        return uniquePaidQrs.stream()
                .collect(Collectors.groupingBy(
                        Qr::getQrPaymentDate,
                        Collectors.collectingAndThen(Collectors.averagingDouble(qr -> qr.getAmount().doubleValue()),
                                BigDecimal::valueOf)));
    }

    public Map<LocalDate, Long> getCntTransactions(String fundUsername) {
        List<Qr> uniquePaidQrs = preprocessingQrs(fundUsername);
        return uniquePaidQrs.stream()
                .collect(Collectors.groupingBy(Qr::getQrPaymentDate,
                        Collectors.counting()));
    }

}
