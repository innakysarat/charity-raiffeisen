package com.project.raif.services;

import com.project.raif.auth.PasswordConfig;
import com.project.raif.models.entity.Fund;
import com.project.raif.repositories.FundRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class FundService {

    private final FundRepository fundRepository;
    PasswordConfig passwordConfig;

    public Fund getFund(String login) {
        return fundRepository.findByLogin(login);
    }

    public Fund create(Fund fund) {
        Fund fundExists = fundRepository.findByLoginAndTitle(fund.getLogin(), fund.getTitle());
        if (fundExists != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Fund with such login and title already exists");
        }
        String encodedPassword = passwordConfig.passwordEncoder().encode(fund.getPassword());
        fund.setPassword(encodedPassword);
        return fundRepository.save(fund);
    }

    public Long delete(Long fundId) {
        Optional<Fund> fund = fundRepository.findById(fundId);
        if (fund.isPresent()) {
            fundRepository.deleteById(fundId);
            return fundId;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Fund with such id is not found");
        }
    }
}
