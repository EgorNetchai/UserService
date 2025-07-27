package ru.aston.intensive.kafkaproducer.event;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import ru.aston.intensive.common.dto.UserNotificationDto;
import ru.aston.intensive.springrestuserservice.util.KafkaOperationException;

/**
 * Компонент для отправки сообщений в Kafka.
 */
@Component
public class EventSender {

    private static final Logger log = LoggerFactory.getLogger(EventSender.class);

    /**
     * Название топика Kafka для отправки сообщений.
     */
    @Value("${kafka.topicName:user-event}")
    private String topicName;

    /**
     * Номер партиции для отправки сообщений.
     */
    @Value("1")
    private int partition;

    /**
     * Шаблон Kafka для отправки сообщений.
     */
    private final KafkaTemplate<String, UserNotificationDto> kafkaTemplate;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param kafkaTemplate Шаблон Kafka для отправки сообщений
     */
    @Autowired
    EventSender(KafkaTemplate<String, UserNotificationDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Отправляет сообщение в Kafka.
     *
     * @param userNotificationDto DTO с информацией о пользователе
     */
    @CircuitBreaker(name = "KafkaCircuitBreaker", fallbackMethod = "fallbackKafkaOperation")
    public void sendMessage(UserNotificationDto userNotificationDto) {
        Message<UserNotificationDto> message = MessageBuilder.withPayload(userNotificationDto)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .setHeader(KafkaHeaders.PARTITION, partition)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();

        kafkaTemplate.send(message);
    }

    /**
     * Fallback-метод для обработки сбоев Kafka.
     *
     * @param userNotificationDto DTO с информацией о пользователе
     * @param t                   исключение, вызвавшее сбой
     *
     * @throws KafkaOperationException для передачи ошибки в GlobalExceptionHandler
     */
    public void fallbackKafkaOperation(UserNotificationDto userNotificationDto, Throwable t) {
        log.error("Не удалось отправить сообщения в Kafka для пользователя {}: {}",
                userNotificationDto.getEmail(), t.getMessage());

        throw new KafkaOperationException("Не удалось отправить сообщение в Kafka", t);
    }
}
