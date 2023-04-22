package com.project.raif.services;

import com.project.raif.models.dto.WidgetRequestDto;
import com.project.raif.models.entity.Fund;
import com.project.raif.models.entity.Widget;
import com.project.raif.repositories.FundRepository;
import com.project.raif.repositories.WidgetRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class WidgetService {
    private final WidgetRepository widgetRepository;
    private final FundRepository fundRepository;

    public Long create(WidgetRequestDto widgetRequest, String username) {
        String merchantId = "MA977181";
        Fund fund = fundRepository.findByLogin(username);
        Widget widget = new Widget(merchantId, widgetRequest.getTemplateId(), widgetRequest.getTemplateProps());
        fund.addWidget(widget);
        widget.assignFund(fund);
        fundRepository.save(fund);
        widgetRepository.save(widget);
        return widget.getId();
    }

    public Widget getWidget(Long widgetId) {
        return widgetRepository.findById(widgetId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Widget with such id is not found"));
    }

    public Long delete(Long widgetId) {
        Optional<Widget> widget = widgetRepository.findById(widgetId);
        if (widget.isPresent()) {
            widgetRepository.deleteById(widgetId);
            return widgetId;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Widget with such id is not found");
        }
    }
    public List<Long> getMyWidgets(String username) {
        Fund fund = fundRepository.findByLogin(username);
        return fund.getWidgets().stream().map(
                Widget::getId
        ).collect(Collectors.toList());
    }
}
