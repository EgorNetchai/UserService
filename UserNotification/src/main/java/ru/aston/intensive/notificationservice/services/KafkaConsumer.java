package ru.aston.intensive.notificationservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.intensive.common.dto.UserNotificationDto;

/**
 * Компонент для обработки сообщений из Kafka.
 * Слушает события пользователя из топика Kafka и инициирует отправку email-уведомлений.
 */
@Component
public class KafkaConsumer {

    EmailServiceImpl emailService;

    @Autowired
    public KafkaConsumer(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    /**
     * Слушает события пользователя из топика Kafka и отправляет email.
     *
     * @param userNotificationDto событие связанное с пользователем
     */
    @KafkaListener(topics = "user-event", groupId = "email-service")
    public void listenUserEvents(UserNotificationDto userNotificationDto) {
        emailService.sendEmail(userNotificationDto);
    }
}
