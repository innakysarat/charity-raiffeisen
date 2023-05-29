package com.project.raif.controllers;

import com.project.raif.models.dto.WidgetRequestDto;
import com.project.raif.models.entity.Widget;
import com.project.raif.services.WidgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Создание виджета")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Виджет успешно создан")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public Long create(@RequestBody WidgetRequestDto widgetRequestDto) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? null : (String) authentication.getPrincipal();
        if (!Objects.equals(username, "anonymousUser")) {
            return widgetService.create(widgetRequestDto, username);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No access to creating widgets"
            );
        }
    }

    @DeleteMapping("/{widgetId}")
    @Operation(summary = "Удаление виджета")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Виджет успешно удален")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public Long delete(@PathVariable Long widgetId) {
        return widgetService.delete(widgetId);
    }

    @GetMapping("/get/{widgetId}")
    @Operation(summary = "Получение виджета по идентификатору")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Виджет получен")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public Widget getWidget(@PathVariable Long widgetId) {
        return widgetService.getWidget(widgetId);
    }

    @GetMapping("/widget/my")
    @Operation(summary = "Получение списка созданных виджетов")
    @ApiResponse(responseCode = "200", description = "Метод завершил работу. Список виджетов получен")
    @ApiResponse(responseCode = "400", description = "Бизнес-ошибка")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
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
