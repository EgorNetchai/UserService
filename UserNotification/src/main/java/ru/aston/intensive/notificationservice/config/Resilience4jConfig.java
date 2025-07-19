package ru.aston.intensive.notificationservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация CircuitBreaker для обработки сбоев внешних сервисов.
 * Предоставляет бины для CircuitBreaker'ов базы данных и отправки email.
 */
@Configuration
public class Resilience4jConfig {

    /**
     * Создаёт CircuitBreaker для операций с базой данных.
     *
     * @return объект CircuitBreaker для обработки сбоев базы данных
     */
    @Bean
    public CircuitBreaker databaseCircuitBreaker() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .minimumNumberOfCalls(3)
                .slidingWindowSize(5)
                .failureRateThreshold(70.0f)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);

        return circuitBreakerRegistry.circuitBreaker("DatabaseCircuitBreaker");
    }

    /**
     * Создаёт CircuitBreaker для операций отправки email.
     *
     * @return объект CircuitBreaker для обработки сбоев отправки email
     */
    @Bean
    public CircuitBreaker emailSenderCircuitBreaker() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .minimumNumberOfCalls(3)
                .slidingWindowSize(5)
                .failureRateThreshold(50.0f)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);

        return circuitBreakerRegistry.circuitBreaker("EmailSenderCircuitBreaker");
    }
}
