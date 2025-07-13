package ru.aston.intensive.notificationservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки OpenAPI документации.
 * Определяет настройки для генерации спецификации API с использованием SpringDoc.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Создает и настраивает объект OpenAPI для документации API.
     *
     * @return Настроенный объект OpenAPI с информацией о приложении
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Management API")
                        .version("1.0")
                        .description("API для управления пользователями")
                );
    }
}
