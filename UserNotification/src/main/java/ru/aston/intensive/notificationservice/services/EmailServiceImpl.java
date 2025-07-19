package ru.aston.intensive.notificationservice.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.intensive.common.dto.UserNotificationDto;
import ru.aston.intensive.notificationservice.advice.GlobalExceptionHandler;
import ru.aston.intensive.notificationservice.model.EmailNotificationEntity;
import ru.aston.intensive.notificationservice.repository.EmailRepository;
import ru.aston.intensive.notificationservice.util.DatabaseOperationException;
import ru.aston.intensive.notificationservice.util.EmailNotFoundException;
import ru.aston.intensive.notificationservice.util.EmailSendOperationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для управления email-уведомлениями.
 * Обрабатывает отправку email и взаимодействие с базой данных уведомлений.
 */
@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final MessageService messageService;
    private final EmailRepository emailRepository;
    private final MailSender mailSender;

    /**
     * Конструктор для инициализации зависимостей сервиса.
     *
     * @param messageService  сервис для создания сообщений
     * @param emailRepository репозиторий для работы с уведомлениями
     * @param mailSender      компонент для отправки email
     */
    @Autowired
    EmailServiceImpl(MessageService messageService, EmailRepository emailRepository, MailSender mailSender) {
        this.messageService = messageService;
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
    }

    /**
     * Отправляет email-уведомление и сохраняет его в базе данных.
     * Устанавливает статус "SENT" при успешной отправке или "FAILED" при сбое.
     *
     * @param event данные о событии пользователя
     */
    @Override
    @CircuitBreaker(name = "EmailSenderCircuitBreaker", fallbackMethod = "fallbackSendOperation")
    public void sendEmail(UserNotificationDto event) {
        SimpleMailMessage message = messageService.createMessage(event);
        EmailNotificationEntity emailEntity = new EmailNotificationEntity();

        emailEntity.setEmail(event.getEmail());
        emailEntity.setEventType(event.getEventType());
        emailEntity.setTimeStamp(LocalDateTime.now());

        try {
            mailSender.send(message);
            emailEntity.setStatus("SENT");
        } catch (Exception e) {
            emailEntity.setStatus("FAILED");
        } finally {
            emailRepository.save(emailEntity);
        }
    }

    /**
     * Fallback-метод для обработки сбоев отправки email.
     *
     * @param t исключение, вызвавшее сбой
     *
     * @throws EmailSendOperationException для передачи ошибки в {@link GlobalExceptionHandler}
     */
    public Object fallbackSendOperation(Throwable t) {
        log.error("Ошибка при отправке сообщения {}", t.getMessage());
        throw new EmailSendOperationException("Ошибка отправки сообщения", t);
    }

    /**
     * Находит email-уведомление по идентификатору.
     *
     * @param id идентификатор уведомления
     *
     * @return объект уведомления {@link EmailNotificationEntity}
     *
     * @throws EmailNotFoundException если уведомление не найдено
     */
    @Override
    @CircuitBreaker(name = "DatabaseCircuitBreaker", fallbackMethod = "fallbackDatabaseOperation")
    public EmailNotificationEntity findEmail(Long id) {
        Optional<EmailNotificationEntity> foundEmail = emailRepository.findById(id);

        return foundEmail.orElseThrow(EmailNotFoundException::new);
    }

    /**
     * Возвращает список всех email-уведомлений.
     *
     * @return список объектов уведомлений {@link EmailNotificationEntity}
     *
     * @throws EmailNotFoundException если список уведомлений пуст
     */
    @Override
    @CircuitBreaker(name = "DatabaseCircuitBreaker", fallbackMethod = "fallbackDatabaseOperation")
    public List<EmailNotificationEntity> findAll() throws EmailNotFoundException {
        List<EmailNotificationEntity> emailList = emailRepository.findAll();

        if (emailList.isEmpty()) {
            throw new EmailNotFoundException();
        }

        return emailList;
    }

    /**
     * Удаляет email-уведомление по идентификатору.
     *
     * @param id идентификатор уведомления
     *
     * @throws EmailNotFoundException если уведомление не найдено
     */
    @Override
    @CircuitBreaker(name = "DatabaseCircuitBreaker", fallbackMethod = "fallbackDatabaseOperation")
    public void deleteEmail(Long id) {
        if (!emailRepository.existsById(id)) {
            throw new EmailNotFoundException();
        }

        emailRepository.deleteById(id);
    }

    /**
     * Fallback-метод для обработки сбоев операций с базой данных.
     *
     * @param t исключение, вызвавшее сбой
     *
     * @throws DatabaseOperationException для передачи ошибки в {@link GlobalExceptionHandler}
     */
    public Object fallbackDatabaseOperation(Throwable t) {
        log.error("Ошибка операции с базой данных {}", t.getMessage());

        throw new DatabaseOperationException("Не удалось выполнить операцию с базой данных", t);
    }
}
