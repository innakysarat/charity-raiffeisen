package com.project.raif.controllers;

import com.project.raif.models.entity.Fund;
import com.project.raif.services.FundService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/fund")
@Controller
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FundController {
    private final FundService fundService;

    @PostMapping
    public Fund create(@RequestBody Fund fund) {
        return fundService.create(fund);
    }

    @DeleteMapping("/{fundId}")
    public Long delete(@PathVariable Long fundId) {
        return fundService.delete(fundId);
    }
}
