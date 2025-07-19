package ru.aston.intensive.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Конфигурация CircuitBreaker для обработки сбоев внешних сервисов.
 * Предоставляет бины для CircuitBreaker'ов базы данных и Kafka.
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
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5)
                .failureRateThreshold(50.0f)
                .slowCallRateThreshold(65.0f)
                .slowCallDurationThreshold(Duration.ofSeconds(3))
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);

        return circuitBreakerRegistry.circuitBreaker("DatabaseCircuitBreaker");
    }

    /**
     * Создаёт CircuitBreaker для операций с Kafka.
     *
     * @return объект CircuitBreaker для обработки сбоев Kafka
     */
    @Bean
    public CircuitBreaker kafkaCircuitBreaker() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5)
                .failureRateThreshold(50.0f)
                .slowCallRateThreshold(65.0f)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);

        return circuitBreakerRegistry.circuitBreaker("KafkaCircuitBreaker");
    }
}
