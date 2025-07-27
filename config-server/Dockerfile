FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY --from=builder /app/target/config-server-0.0.1-SNAPSHOT.jar app.jar
COPY /src/main/resources/application.yaml .
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]