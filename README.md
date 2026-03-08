# Migration Tracking System - Backend API

This is the Spring Boot REST API for the Migration Tracking System. It provides secured endpoints to manage migrant records, handle user authentication, and generate downloadable reports.

## Tech Stack
* **Java / Spring Boot:** Core framework
* **Spring Security & JWT:** Stateless API authentication
* **PostgreSQL:** Relational database
* **OpenAPI (Swagger):** Interactive API documentation
* **iText / OpenPDF:** PDF report generation

## Prerequisites
Before running this project, ensure you have the following installed:
* Java Development Kit (JDK) 17 or higher
* Maven
* PostgreSQL

## Local Environment Setup

For security reasons, database credentials are not hardcoded in the repository. You must configure the following environment variables on your local machine before starting the server:

* `DB_URL` = `jdbc:postgresql://localhost:5432/your_database_name`
* `DB_USERNAME` = `your_postgres_username`
* `DB_PASSWORD` = `your_postgres_password`

*(If using IntelliJ IDEA, you can add these in the Run/Debug Configurations under "Environment variables".)*

## Running the Application
1. Clone the repository to your local machine.
2. Ensure your local PostgreSQL server is running and the database is created.
3. Set your environment variables.
4. Run the application using your IDE or via the terminal:
   ```bash
   mvn spring-boot:run
