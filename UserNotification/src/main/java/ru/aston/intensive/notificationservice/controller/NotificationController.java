package ru.aston.intensive.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.intensive.notificationservice.dto.EmailNotificationDto;
import ru.aston.intensive.notificationservice.services.EmailMapper;
import ru.aston.intensive.notificationservice.services.EmailService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST-контроллер для управления email-уведомлениями.
 * Обрабатывает запросы на получение и удаление уведомлений.
 */
@RestController
@RequestMapping("/notifications")
@Tag(name = "Сообщения", description = "Отправка сообщений на email пользователя")
public class NotificationController {

    private final EmailService emailService;
    private final EmailMapper emailMapper;

    /**
     * Конструктор контроллера уведомлений.
     *
     * @param emailService сервис для работы с email-уведомлениями
     * @param emailMapper  маппер для преобразования сущностей в DTO
     */
    @Autowired
    public NotificationController(EmailService emailService, EmailMapper emailMapper) {
        this.emailService = emailService;
        this.emailMapper = emailMapper;
    }

    /**
     * Возвращает список всех email-уведомлений с HATEOAS-ссылками.
     *
     * @return коллекция DTO уведомлений с HATEOAS-ссылками
     */
    @GetMapping()
    @Operation(
            summary = "Получение всех писем",
            description = "Получение всех писем отправленных пользователям с уведомлениями о создании или удалении"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Все письма получены", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmailNotificationDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Страница не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера"),
            @ApiResponse(responseCode = "503", description = "Сервис недоступен")
    })
    public CollectionModel<EmailNotificationDto> findAll() {
        List<EmailNotificationDto> emailList = emailService.findAll().stream()
                .map(emailEntity -> {
                    EmailNotificationDto emailDto = emailMapper.toEmailDto(emailEntity);

                    emailDto.add(linkTo(methodOn(NotificationController.class)
                            .findEmail(emailEntity.getId())).withSelfRel());
                    emailDto.add(linkTo(methodOn(NotificationController.class).findAll()).withRel("notifications"));
                    emailDto.add(linkTo(methodOn(NotificationController.class)
                            .deleteEmail(emailEntity.getId())).withSelfRel());

                    return emailDto;
                })
                .toList();

        return CollectionModel.of(emailList,
                linkTo(methodOn(NotificationController.class).findAll()).withSelfRel());
    }

    /**
     * Возвращает email-уведомление по идентификатору с HATEOAS-ссылками.
     *
     * @param id идентификатор уведомления
     *
     * @return объект DTO уведомления с HATEOAS-ссылками
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Получение письма",
            description = "Получение одного письма по его id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Письмо получено", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmailNotificationDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Страница не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера"),
            @ApiResponse(responseCode = "503", description = "Сервис недоступен")
    })
    public EntityModel<EmailNotificationDto> findEmail(
            @Parameter(description = "id письма") @PathVariable("id") Long id
    ) {
        EmailNotificationDto emailDto = emailMapper.toEmailDto(emailService.findEmail(id));

        emailDto.add(linkTo(methodOn(NotificationController.class).findAll()).withSelfRel());
        emailDto.add(linkTo(methodOn(NotificationController.class).deleteEmail(id)).withRel("delete"));

        return EntityModel.of(emailDto);
    }

    /**
     * Удаляет email-уведомление по идентификатору.
     *
     * @param id идентификатор уведомления
     *
     * @return ответ с кодом 204 (No Content) при успешном удалении
     */
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Удаление письма",
            description = "Удаление одного письма по его id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "no content", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmailNotificationDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Страница не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера"),
            @ApiResponse(responseCode = "503", description = "Сервис недоступен")
    })
    public ResponseEntity<Void> deleteEmail(
            @Parameter(description = "id письма") @PathVariable("id") Long id
    ) {
        emailService.deleteEmail(id);

        return ResponseEntity.noContent().build();
    }
}
