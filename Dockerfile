FROM eclipse-temurin:23 AS builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:23
WORKDIR /app
COPY --from=builder /app/target/*-jar-with-dependencies.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
