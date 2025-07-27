FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app/notification-service
COPY pom.xml .
COPY src ./src
COPY .m2/repository /root/.m2/repository
RUN  apk add --no-cache maven && mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY --from=builder /app/notification-service/target/NotificationService-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/logback-spring.xml .
COPY src/main/resources/bootstrap.yaml .
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]