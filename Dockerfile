# Stage 1: Build the Next.js app
FROM node:18-alpine AS nextjs-build

# Install git
RUN apk add --no-cache git

# Clone the Next.js repository
RUN git clone https://github.com/coralCoralCoralCoral/ui.git /app

# Set working directory
WORKDIR /app

# Install dependencies
RUN npm install

RUN --mount=type=secret,id=mapbox_api_key \
    echo "NEXT_PUBLIC_MAPBOX_API_KEY=$(cat /run/secrets/mapbox_api_key)" > .env.production.local

RUN npm run build

# Use a base image with Gradle and OpenJDK 17 installed
FROM gradle:8.10.2-jdk17 AS build

# Set working directory
WORKDIR /app

# Copy your Gradle build files (e.g., build.gradle, settings.gradle) to the container
COPY build.gradle settings.gradle ./

# Copy the Gradle wrapper (if present) to the container
COPY gradlew gradlew
COPY gradle gradle
# Now copy the source code and build the app
COPY src src

COPY --from=nextjs-build /app/out src/main/resources/static

# Download dependencies (this will cache dependencies unless build.gradle changes)
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon --stacktrace

# Build the Spring Boot application ...

# Use a smaller JDK 17 image to run the Spring Boot application
FROM openjdk:17-jdk-slim AS runtime

# Set working directory for runtime container
WORKDIR /app

# Copy the jar file from the build stage to the runtime container
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port (default Spring Boot is 8080)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
