package ru.aston.intensive.notificationservice.services;

import ru.aston.intensive.common.dto.UserNotificationDto;
import ru.aston.intensive.notificationservice.model.EmailNotificationEntity;

import java.util.List;

/**
 * Интерфейс сервиса для работы с email-уведомлениями.
 */
public interface EmailService {

    /**
     * Отправляет email-уведомление на основе данных о событии.
     *
     * @param event данные о событии пользователя
     */
    void sendEmail(UserNotificationDto event);

    /**
     * Находит email-уведомление по идентификатору.
     *
     * @param id идентификатор уведомления
     *
     * @return объект уведомления {@link EmailNotificationEntity}
     */
    EmailNotificationEntity findEmail(Long id);

    /**
     * Возвращает список всех email-уведомлений.
     *
     * @return список объектов уведомлений {@link EmailNotificationEntity}
     */
    List<EmailNotificationEntity> findAll();

    /**
     * Удаляет email-уведомление по идентификатору.
     *
     * @param id идентификатор уведомления
     */
    void deleteEmail(Long id);
}
