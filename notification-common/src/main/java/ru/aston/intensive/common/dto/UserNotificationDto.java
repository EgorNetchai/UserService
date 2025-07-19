package ru.aston.intensive.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO-класс, представляющий данные уведомления пользователя.
 * Содержит информацию о email и типе события, поддерживает HATEOAS-ссылки.
 */
public class UserNotificationDto extends RepresentationModel<UserNotificationDto> {
    @JsonProperty
    @Schema(description = "Email пользователя", example = "johndoe@exmaple.com")
    private String email;

    @JsonProperty
    @Schema(description = "Тип события у пользователя", examples = {"CREATED", "DELETED"})
    private String eventType;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
