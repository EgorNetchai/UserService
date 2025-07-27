package ru.aston.intensive.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Основной класс приложения для запуска API Gateway.
 * Обеспечивает маршрутизацию запросов к микросервисам в архитектуре.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApiApplication {

	/**
	 * Точка входа в приложение.
	 * Запускает Spring Boot приложение с конфигурацией API Gateway.
	 *
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayApiApplication.class, args);
	}
}
