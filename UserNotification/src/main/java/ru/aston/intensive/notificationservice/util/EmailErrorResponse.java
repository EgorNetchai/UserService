package ru.aston.intensive.notificationservice.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс для предоставления ответа об ошибке, связанной с email.
 * Содержит сообщение об ошибке и временную метку
 */
public class EmailErrorResponse {
    private String message;
    private String timestamp;

    /**
     * Конструктор для создания объекта ошибки с сообщением и временной меткой
     *
     * @param message   сообщение об ошибке
     * @param timestamp временная метка
     */
    public EmailErrorResponse(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Получает сообщение об ошибке.
     *
     * @return сообщение об ошибке
     */
    public String getMessage() {
        return message;
    }

    /**
     * Устанавливает сообщение об ошибке.
     *
     * @param message сообщение об ошибке
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Получает временную метку ошибки.
     *
     * @return временная метка в формате строки
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Устанавливает временную метку ошибки.
     *
     * @param timestamp временная метка в формате строки
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
