# Use an appropriate JDK 23 image if available
FROM openjdk:23-jdk-slim

WORKDIR /app
COPY target/APPOINTMENTS_ASTURIAS-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
