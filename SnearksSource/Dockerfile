# syntax=docker/dockerfile:1

FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /workspace

# Copia directa de archivos
COPY . .

# Compilación usando el comando gradle nativo
RUN gradle bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia el JAR generado
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]