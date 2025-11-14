# Use Eclipse Temurin 21 as base image (official OpenJDK successor)
FROM eclipse-temurin:21-jdk-jammy

# Install curl for health checks and update CA certificates
RUN apt-get update && apt-get install -y curl ca-certificates && \
    update-ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .
COPY gradle/libs.versions.toml gradle/

# Copy source code
COPY app/ app/
COPY db/ db/
COPY shared/ shared/
COPY web/ web/
COPY react-web/ react-web/

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the backend and React frontend
RUN ./gradlew :app:shadowJar :react-web:assemble --no-daemon

# Expose port 8080
EXPOSE 8080

# Run the Fat JAR
CMD ["java", "-jar", "/app/app/build/libs/app-0.0.1-all.jar"]