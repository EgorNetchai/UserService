package ru.aston.intensive.springrestuserservice.util;

/**
 * Исключение, выбрасываемое при сбоях операций с Kafka.
 */
public class KafkaOperationException extends RuntimeException {

    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение об ошибке
     */
    public KafkaOperationException(String message) {
        super(message);
    }

    /**
     * Конструктор с сообщением об ошибке и причиной.
     *
     * @param message сообщение об ошибке
     * @param cause   причина возникновения исключения
     */
    public KafkaOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
