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
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FundService {

    // TODO: проверить все типы ошибок (где 400, где 401 и тд)
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
        paidQrs = paidQrs.stream().filter(qr -> Objects.equals(qr.getFund().getLogin(), fundUsername))
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


    public List<TreeMap<LocalDate, BigDecimal>> getStatisticsByDateRange(String fundUsername,
                                                                         String startDate,
                                                                         String endDate) {
        List<Qr> uniquePaidQrs = preprocessingQrs(fundUsername);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        List<Qr> qrs = uniquePaidQrs.stream()
                .filter(qr -> qr.getQrPaymentDate().isAfter(start)
                        && qr.getQrPaymentDate().isBefore(end)
                        || qr.getQrPaymentDate().isEqual(start)
                        || qr.getQrPaymentDate().isEqual(end))
                .collect(Collectors.toList());

        List<TreeMap<LocalDate, BigDecimal>> data = new ArrayList<>();
        data.add(new TreeMap<>(getIncome(fundUsername, qrs)));
        data.add(new TreeMap<>(getAvgCheque(fundUsername, qrs)));
        Map<LocalDate, BigDecimal> cntTransactions = getCntTransactions(fundUsername, qrs)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> BigDecimal.valueOf(entry.getValue())));
        data.add(new TreeMap<>(cntTransactions));
        return data;
    }

    public TreeMap<LocalDate, BigDecimal> getIncome(String fundUsername, List<Qr> qrs) {
        if (qrs == null) {
            qrs = preprocessingQrs(fundUsername);
        }
        return new TreeMap<>(qrs.stream()
                .collect(Collectors.groupingBy(
                        qr -> qr.getQrPaymentDate()
                                .with(ADJUSTERS.get("day")),
                        Collectors.mapping(Qr::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)))));
    }

    public TreeMap<LocalDate, BigDecimal> getAvgCheque(String fundUsername, List<Qr> qrs) {
        if (qrs == null) {
            qrs = preprocessingQrs(fundUsername);
        }
        return new TreeMap<>(qrs.stream()
                .collect(Collectors.groupingBy(
                        Qr::getQrPaymentDate,
                        Collectors.collectingAndThen(Collectors.averagingDouble(qr -> qr.getAmount().doubleValue()),
                                BigDecimal::valueOf))));
    }

    public TreeMap<LocalDate, Long> getCntTransactions(String fundUsername, List<Qr> qrs) {
        if (qrs == null) {
            qrs = preprocessingQrs(fundUsername);
        }
        return new TreeMap<>(qrs.stream()
                .collect(Collectors.groupingBy(Qr::getQrPaymentDate,
                        Collectors.counting())));
    }

    public TreeMap<LocalDate, Long> getCntSubscriptions(String fundUsername, List<Qr> qrs) {
        if (qrs == null) {
            qrs = preprocessingQrs(fundUsername);
        }
        return new TreeMap<>(qrs.stream()
                .filter(qr -> qr.getSubscriptionId() != null)
                .collect(Collectors.groupingBy(Qr::getQrPaymentDate,
                        Collectors.counting())));
    }

    public TreeMap<LocalDate, BigDecimal> getSubscriptionAvgCheque(String fundUsername, List<Qr> qrs) {
        if (qrs == null) {
            qrs = preprocessingQrs(fundUsername);
        }
        return new TreeMap<>(qrs.stream()
                .filter(qr -> qr.getSubscriptionId() != null)
                .collect(Collectors.groupingBy(
                        Qr::getQrPaymentDate,
                        Collectors.collectingAndThen(Collectors.averagingDouble(qr -> qr.getAmount().doubleValue()),
                                BigDecimal::valueOf))));
    }

    public Map<String, BigDecimal> getOneDayStatistics(String fundUsername, String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        List<Qr> uniquePaidQrs = preprocessingQrs(fundUsername);

        Map<String, BigDecimal> statistics = new HashMap<>();
        // Filter and count total records
        long totalCount = uniquePaidQrs.stream()
                .filter(qr -> qr.getQrPaymentDate().equals(localDate))
                .count();
        statistics.put("Общее количество платежей за день", BigDecimal.valueOf(totalCount));

        BigDecimal amount = uniquePaidQrs.stream()
                .filter(qr -> qr.getQrPaymentDate().equals(localDate))
                .map(Qr::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.put("Общая сумма платежей за день", amount);

        BigDecimal averageCheque = uniquePaidQrs.stream()
                .filter(qr -> qr.getQrPaymentDate().equals(localDate))
                .collect(Collectors.collectingAndThen(Collectors.averagingDouble(qr -> qr.getAmount().doubleValue()),
                        BigDecimal::valueOf));
        statistics.put("Средний чек за день", averageCheque.setScale(2, RoundingMode.HALF_UP));

        return statistics;
    }

}
