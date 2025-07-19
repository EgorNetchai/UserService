package ru.aston.intensive.kafkaproducer.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.aston.intensive.common.dto.UserNotificationDto;
import ru.aston.intensive.kafkaproducer.event.EventSender;
import ru.aston.intensive.springrestuserservice.models.UserEntity;
import ru.aston.intensive.springrestuserservice.services.UserMapper;
import ru.aston.intensive.springrestuserservice.services.UsersServiceCrud;

/**
 * Аспект для публикации событий создания и удаления пользователей в Kafka.
 */
@Aspect
@Component
public class KafkaEventPublishingAspect {

    private final EventSender eventSender;
    private final UsersServiceCrud usersServiceCrud;
    private final UserMapper userMapper;

    @Autowired
    public KafkaEventPublishingAspect(EventSender eventSender,
                                      UsersServiceCrud usersServiceCrud,
                                      UserMapper userMapper) {
        this.eventSender = eventSender;
        this.usersServiceCrud = usersServiceCrud;
        this.userMapper = userMapper;
    }

    /**
     * Публикует событие создания пользователя после успешного сохранения.
     *
     * @param userEntity Сущность пользователя
     */
    @AfterReturning(
            pointcut = "execution(* ru.aston.intensive.springrestuserservice.services." +
            "UsersServiceCrud.save(..)) && args(userEntity)"
    )
    public void publishUserCreatedEvent(UserEntity userEntity) {
        UserNotificationDto userNotificationDto = userMapper.toUserNotificationDto(userEntity);
        userNotificationDto.setEventType("CREATED");
        eventSender.sendMessage(userNotificationDto);
    }

    /**
     * Публикует событие удаления пользователя перед выполнением операции удаления.
     *
     * @param id Идентификатор пользователя
     */
    @Before(
            "execution(* ru.aston.intensive.springrestuserservice.services." +
            "UsersServiceCrud.delete(..)) && args(id)"
    )
    public void publishUserDeletedEvent(Long id) {
        UserEntity user = usersServiceCrud.findOne(id);
        UserNotificationDto userNotificationDto = userMapper.toUserNotificationDto(user);
        userNotificationDto.setEventType("DELETED");
        eventSender.sendMessage(userNotificationDto);
    }
}