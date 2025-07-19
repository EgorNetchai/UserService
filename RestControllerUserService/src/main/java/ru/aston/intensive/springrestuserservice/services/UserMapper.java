package ru.aston.intensive.springrestuserservice.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.aston.intensive.common.dto.UserNotificationDto;
import ru.aston.intensive.springrestuserservice.dto.UserDto;
import ru.aston.intensive.springrestuserservice.models.UserEntity;

/**
 * Интерфейс маппера для преобразования объектов между сущностью UserEntity, DTO UserDto и
 * UserNotificationDto с использованием библиотеки MapStruct.
 * Реализует преобразования между различными моделями данных приложения.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует сущность UserEntity в DTO UserDto.
     *
     * @param userEntity Сущность пользователя для преобразования
     *
     * @return DTO пользователя с игнорированием поля links
     */
    @Mapping(target = "links", ignore = true)
    UserDto toUserDto(UserEntity userEntity);

    /**
     * Преобразует DTO UserDto в сущность UserEntity.
     *
     * @param userDto DTO пользователя для преобразования
     *
     * @return Сущность пользователя
     */
    UserEntity toUserEntity(UserDto userDto);

    /**
     * Преобразует сущность UserEntity в DTO UserNotificationDto.
     *
     * @param userEntity Сущность пользователя для преобразования
     *
     * @return DTO уведомления пользователя с игнорированием поля eventType
     */
    @Mapping(target = "eventType", ignore = true)
    UserNotificationDto toUserNotificationDto(UserEntity userEntity);

}
