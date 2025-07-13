package ru.aston.intensive.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.intensive.common.dto.UserNotificationDto;
import ru.aston.intensive.notificationservice.services.EmailService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST-контроллер для управления уведомлениями.
 * Обрабатывает запросы на отправку email-уведомлений пользователям.
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Сообщения", description = "Отправка сообщений на email пользователя")
public class NotificationController {

    private final EmailService emailService;

    /**
     * Конструктор контроллера уведомлений.
     *
     * @param emailService Сервис для отправки email-уведомлений
     */
    @Autowired
    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Отправляет email-уведомление пользователю.
     *
     * @param event Данные события для отправки уведомления
     *
     * @return HTTP-статус 201 и объект уведомления
     */
    @PostMapping("/send")
    @Operation(
            summary = "Отправка email",
            description = "Отправка email пользователю с сообщением о создании или удалении"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Письмо отправлено", content = {
                    @Content(mediaType = "application/json",
                             schema = @Schema(implementation = UserNotificationDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Не удалось отправить письмо"),
            @ApiResponse(responseCode = "404", description = "Страница не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ResponseEntity<UserNotificationDto> sendEmail(
            @Parameter(description = "Данные события для отправки") @RequestBody UserNotificationDto event
    ) {
        emailService.sendEmail(event);

        event.add(linkTo(methodOn(NotificationController.class).sendEmail(event)).withSelfRel());

        return ResponseEntity.status(201).body(event);
    }
}
