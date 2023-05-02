package com.project.raif.services;

import com.project.raif.auth.PasswordConfig;
import com.project.raif.exception.ApiException;
import com.project.raif.exception.ErrorCode;
import com.project.raif.models.entity.Fund;
import com.project.raif.models.entity.Qr;
import com.project.raif.repositories.FundRepository;
import com.project.raif.repositories.QrRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class FundService {

    private final FundRepository fundRepository;
    private final QrRepository qrRepository;
    PasswordConfig passwordConfig;


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

    public Long delete(Long fundId) {
        Optional<Fund> fund = fundRepository.findById(fundId);
        if (fund.isPresent()) {
            fundRepository.deleteById(fundId);
            return fundId;
        } else {
            throw new ApiException(ErrorCode.ERROR_NOT_FOUND_FUND, ErrorCode.ERROR_NOT_FOUND_FUND.getMessage());
        }
    }

    public List<Qr> getQrs(String fundUsername) {
        Fund fund = fundRepository.findByLogin(fundUsername).orElseThrow(() ->
                new ApiException(ErrorCode.ERROR_NOT_FOUND_FUND, ErrorCode.ERROR_NOT_FOUND_FUND.getMessage()));
        return qrRepository.findByFund(fund);
    }
}
