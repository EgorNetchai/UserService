package ru.aston.intensive.notificationservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.aston.intensive.notificationservice.util.DatabaseOperationException;
import ru.aston.intensive.notificationservice.util.EmailErrorResponse;
import ru.aston.intensive.notificationservice.util.EmailNotFoundException;
import ru.aston.intensive.notificationservice.util.EmailSendOperationException;

import java.time.LocalDateTime;

/**
 * Глобальный обработчик исключений для REST-контроллеров в приложении уведомлений.
 * Обрабатывает исключения, связанные с операциями email и базы данных.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение {@link EmailNotFoundException}, когда email-уведомление не найдено.
     *
     * @param e исключение, связанное с отсутствием email-уведомления
     *
     * @return объект ответа {@link EmailErrorResponse} с кодом 404 (Not Found)
     */
    @ExceptionHandler
    public ResponseEntity<EmailErrorResponse> handleException(EmailNotFoundException e) {
        EmailErrorResponse response = new EmailErrorResponse(
                e.getMessage() != null ? e.getMessage() : "Пользователь с таким id не найден!",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключение {@link DatabaseOperationException}, связанное с операциями базы данных.
     *
     * @param e исключение, вызванное сбоем базы данных
     *
     * @return объект ответа {@link EmailErrorResponse} с кодом 503 (Service Unavailable)
     */
    @ExceptionHandler
    public ResponseEntity<EmailErrorResponse> handleException(DatabaseOperationException e) {
        EmailErrorResponse response = new EmailErrorResponse(
                e.getMessage() != null ? e.getMessage() : "Ошибка операции с базой данных",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Обрабатывает исключение {@link EmailSendOperationException}, связанное с отправкой email.
     *
     * @param e исключение, вызванное сбоем отправки email
     *
     * @return объект ответа {@link EmailErrorResponse} с кодом 503 (Service Unavailable)
     */
    @ExceptionHandler
    public ResponseEntity<EmailErrorResponse> handleException(EmailSendOperationException e) {
        EmailErrorResponse response = new EmailErrorResponse(
                e.getMessage() != null ? e.getMessage() : "Ошибка отправки сообщения",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
