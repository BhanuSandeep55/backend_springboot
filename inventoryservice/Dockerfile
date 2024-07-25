# Use the OpenJDK 11 JRE slim image as the base image
FROM openjdk:11-jre-slim

# Copy the compiled JAR file into the Docker image
COPY target/inventoryservice-0.0.1-SNAPSHOT.jar inventoryservice.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/inventoryservice.jar"]
