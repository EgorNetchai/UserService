package ru.aston.intensive.config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Основной класс приложения для запуска Spring Cloud Config Server.
 * Предоставляет централизованное управление конфигурациями для микросервисов.
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	/**
	 * Точка входа в приложение.
	 * Запускает Spring Boot приложение с конфигурацией Config Server.
	 *
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
