package ru.aston.intensive.server_discovery_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Основной класс приложения для запуска Eureka Server.
 * Обеспечивает функциональность службы обнаружения сервисов в микросервисной архитектуре.
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

    /**
     * Точка входа в приложение.
     * Запускает Spring Boot приложение с конфигурацией Eureka Server.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
		SpringApplication.run(DiscoveryServiceApplication.class, args);
    }
}
