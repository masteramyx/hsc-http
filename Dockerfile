# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

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

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew build --no-daemon

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["./gradlew", "run", "--no-daemon"]