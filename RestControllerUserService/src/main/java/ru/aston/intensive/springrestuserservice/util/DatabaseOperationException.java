package ru.aston.intensive.springrestuserservice.util;

/**
 * Исключение, выбрасываемое при сбоях операций с базой данных.
 */
public class DatabaseOperationException extends RuntimeException {

    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение об ошибке
     */
    public DatabaseOperationException(String message) {
        super(message);
    }

    /**
     * Конструктор с сообщением об ошибке и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина возникновения исключения
     */
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
