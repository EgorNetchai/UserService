package ru.aston.intensive.notificationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * DTO для представления email-уведомления с поддержкой HATEOAS.
 */
@Schema(description = "Объект передачи данных для сущности EmailNotificationEntity")
public class EmailNotificationDto extends RepresentationModel<EmailNotificationDto> {

    /**
     * Email-адрес получателя уведомления.
     */
    @Schema(description = "email на который отправлено письмо", example = "john.doe@example.com")
    private String email;

    /**
     * Тип события.
     */
    @Schema(description = "Тип события которое произошло с получателем", examples = {"CREATED", "DELETED"})
    private String eventType;

    /**
     * Статус отправки уведомления.
     */
    @Schema(description = "Статус отправки письма", examples = {"SENT", "FAILED"})
    private String status;

    /**
     * Временная метка создания уведомления.
     */
    @Schema(description = "Время отправки письма", example = "2025.07.18 20:00:00")
    private LocalDateTime timeStamp;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
