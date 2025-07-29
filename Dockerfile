# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

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

# Make gradlew executable
RUN chmod +x ./gradlew

# Build only the server components (app, db, shared)
RUN ./gradlew :app:build :db:build :shared:build :web:build --no-daemon

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["./gradlew", "run", "--no-daemon"]