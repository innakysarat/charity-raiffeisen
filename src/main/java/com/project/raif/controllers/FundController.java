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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/fund")
@Controller
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
    public Long delete(@PathVariable Long fundId) {
        // проверка, что нужный человек удаляет
        return fundService.delete(fundId);
    }

    @GetMapping("/qrs")
    public List<Qr> getQrs() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String fundUsername = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(fundUsername, "anonymousUser")) {
            return fundService.getQrs(fundUsername);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No access to list of qrs"
            );
        }
    }
}
