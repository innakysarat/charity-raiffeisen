package com.project.raif.controllers;

import com.project.raif.models.dto.WidgetRequestDto;
import com.project.raif.models.entity.Widget;
import com.project.raif.services.WidgetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/widgets")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class WidgetController {

    private final WidgetService widgetService;

    @PostMapping("/create")
    public Long create(@RequestBody WidgetRequestDto widgetRequestDto) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            return widgetService.create(widgetRequestDto, username);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No access for creating widgets"
            );
        }
    }

    @DeleteMapping("/{widgetId}")
    public Long delete(@PathVariable Long widgetId) {
        return widgetService.delete(widgetId);
    }

    @GetMapping("/get/{widgetId}")
    public Widget getWidget(@PathVariable Long widgetId) {
        return widgetService.getWidget(widgetId);
    }

    @GetMapping("/widget/my")
    public List<Long> getMyWidgets() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            return widgetService.getMyWidgets(username);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No access to widgets of other people"
            );
        }
    }
}
