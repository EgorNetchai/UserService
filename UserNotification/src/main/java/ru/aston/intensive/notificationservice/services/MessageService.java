package ru.aston.intensive.notificationservice.services;

import org.springframework.mail.SimpleMailMessage;
import ru.aston.intensive.common.dto.UserNotificationDto;

/**
 * Интерфейс для формирования сообщения
 */
public interface MessageService {

    /**
     * Формирует сообщение на основе события
     *
     * @param event событие связанное с пользователем
     *
     * @return сформированное сообщение
     */
    SimpleMailMessage createMessage(UserNotificationDto event);
}
