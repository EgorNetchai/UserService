package ru.aston.intensive.notificationservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.aston.intensive.notificationservice.aspects.ExceptionHandlerAspect;
import ru.aston.intensive.notificationservice.aspects.LoggingAspect;

/**
 * Конфигурационный класс для настройки логгеров в приложении.
 * Определяет бины логгеров для аспектов LoggingAspect и ExceptionHandlerAspect.
 */
@Configuration
public class LoggingConfig {

    /**
     * Создает и возвращает логгер для аспекта LoggingAspect.
     *
     * @return Экземпляр логгера для класса LoggingAspect
     */
    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(LoggingAspect.class);
    }

    /**
     * Создает и возвращает логгер для аспекта ExceptionHandlerAspect.
     *
     * @return Экземпляр логгера для класса ExceptionHandlerAspect
     */
    @Bean
    public Logger exceptionHandlerAspectLogger() {
        return LoggerFactory.getLogger(ExceptionHandlerAspect.class);
    }
}