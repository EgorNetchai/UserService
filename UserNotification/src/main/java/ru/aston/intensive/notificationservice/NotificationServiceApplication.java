package ru.aston.intensive.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Основной класс приложения для сервиса уведомлений.
 * Запускает Spring Boot приложение и включает поддержку Kafka.
 */
@SpringBootApplication
@ComponentScan(basePackages = "ru.aston.intensive.notificationservice")
@EnableDiscoveryClient
@EntityScan(basePackages = "ru.aston.intensive.notificationservice.model")
@EnableAspectJAutoProxy
@EnableKafka
public class NotificationServiceApplication {
          public static void main(String[] args) {
            SpringApplication.run(NotificationServiceApplication.class, args);
        }
}