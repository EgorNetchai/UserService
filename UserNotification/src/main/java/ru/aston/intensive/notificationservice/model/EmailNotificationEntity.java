package ru.aston.intensive.notificationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Сущность для хранения информации об email-уведомлениях в базе данных.
 */
@Entity
@Table(name = "email_notifications")
public class EmailNotificationEntity {

    /**
     * Уникальный идентификатор уведомления.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Email-адрес получателя уведомления.
     */
    @Column(name = "email")
    private String email;

    /**
     * Тип события.
     */
    @Column(name = "event")
    private String eventType;

    /**
     * Статус отправки уведомления.
     */
    @Column(name = "status")
    private String status;

    /**
     * Временная метка создания уведомления.
     */
    @Column(name = "created_at")
    private LocalDateTime timeStamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
