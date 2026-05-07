# 1. Use the official Java 21 environment (since your logs showed Java 21!)
FROM eclipse-temurin:21-jdk

# 2. Create a folder inside the cloud server
WORKDIR /app

# 3. Copy all your code from GitHub into the server
COPY . .

# 4. Give permission to the Maven wrapper
RUN chmod +x mvnw

# 5. Build your Spring Boot application
RUN ./mvnw clean package -DskipTests

# 6. Open port 8080 for the frontend to connect
EXPOSE 8080

# 7. Start the server!
CMD ["java", "-jar", "target/migration_project-0.0.1-SNAPSHOT.jar"]