# =========================================================================
# Etapa 1: Compilación del código Kotlin y generación del fat JAR
# =========================================================================
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# =========================================================================
# Etapa 2: Extracción de capas del Fat JAR de Spring Boot
# =========================================================================
FROM eclipse-temurin:21-jre-alpine AS extractor
WORKDIR /staging

COPY --from=builder /workspace/build/libs/*.jar app.jar
# --layers divide en carpetas y --launcher extrae el JarLauncher
RUN java -Djarmode=tools -jar app.jar extract --layers --launcher --destination /extracted

# =========================================================================
# Etapa 3: Imagen final de producción (Runtime mínimo y no-root)
# =========================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S familyhub && adduser -S familyhub -G familyhub
USER familyhub:familyhub

COPY --from=extractor /extracted/dependencies/ ./
COPY --from=extractor /extracted/spring-boot-loader/ ./
COPY --from=extractor /extracted/snapshot-dependencies/ ./
COPY --from=extractor /extracted/application/ ./

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]