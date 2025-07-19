package ru.aston.intensive.notificationservice.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.aston.intensive.notificationservice.dto.EmailNotificationDto;
import ru.aston.intensive.notificationservice.model.EmailNotificationEntity;

/**
 * Интерфейс маппера для преобразования объектов между сущностью EmailNotificationEntity и
 * DTO EmailNotificationDto с использованием библиотеки MapStruct.
 * Реализует преобразования между различными моделями данных приложения.
 */
@Mapper(componentModel = "spring")
public interface EmailMapper {

    /**
     * Преобразует сущность EmailNotificationEntity в DTO EmailNotificationDto
     *
     * @param emailEntity сущность для преобразования
     *
     * @return DTO email с игнорированием поля links
     */
    @Mapping(target = "links", ignore = true)
    EmailNotificationDto toEmailDto(EmailNotificationEntity emailEntity);
}
