package ru.aston.intensive.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.aston.intensive.notificationservice.model.EmailNotificationEntity;

/**
 * Репозиторий для работы с сущностями email-уведомлений в базе данных.
 */
@Repository
public interface EmailRepository extends JpaRepository<EmailNotificationEntity, Long> {
}
