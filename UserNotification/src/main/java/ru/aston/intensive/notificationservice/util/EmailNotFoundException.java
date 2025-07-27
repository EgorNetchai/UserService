package ru.aston.intensive.notificationservice.util;

/**
 * Исключение, выбрасываемое при отсутствии email в базе данных.
 */
public class EmailNotFoundException extends RuntimeException {
    /**
     * Конструктор по умолчанию.
     */
    public EmailNotFoundException() {
        super();
    }
}
