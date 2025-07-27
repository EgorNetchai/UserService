package ru.aston.intensive.notificationservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import ru.aston.intensive.common.dto.UserNotificationDto;

/**
 * Реализация сервиса для создания сообщений email-уведомлений.
 */
@Component
public class MessageServiceImpl implements MessageService {

    /**
     * Адрес отправителя, получаемый из конфигурации.
     */
    @Value("${MAIL_USERNAME}")
    private String fromEmail;

    /**
     * Создает объект сообщения на основе данных о событии пользователя.
     *
     * @param event данные о событии пользователя
     *
     * @return объект сообщения {@link SimpleMailMessage}
     */
    @Override
    public SimpleMailMessage createMessage(UserNotificationDto event) {
        String subject = event.getEventType().equals("CREATED")
                ? "Пользователь создан"
                : "Пользователь удален";
        String text = String.format("Пользователь с email %s %s.",
                event.getEmail(),
                event.getEventType().equals("CREATED") ? "создан" : "удален");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(fromEmail);

        return message;
    }
}