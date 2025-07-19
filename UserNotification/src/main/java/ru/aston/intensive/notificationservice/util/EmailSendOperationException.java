package ru.aston.intensive.notificationservice.util;

/**
 * Исключение, выбрасываемое при сбоях отправки email-уведомления.
 */
public class EmailSendOperationException extends RuntimeException {

    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение об ошибке
     */
    public EmailSendOperationException(String message) {
        super(message);
    }

    /**
     * Конструктор с сообщением об ошибке и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина возникновения исключения
     */
    public EmailSendOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
