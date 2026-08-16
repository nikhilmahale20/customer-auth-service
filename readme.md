# NeoBank - Customer Onboarding & Authentication

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)]()
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)]()
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)]()
[![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)]()
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)]()

A robust customer onboarding and authentication microservice built with **Java** and **Spring Boot**. This service handles secure user registration, login, profile management, and password recovery, providing a solid foundation for NeoBank's banking platform.

## 🏗️ Architecture

![NeoBank Architecture](https://raw.githubusercontent.com/nikhilmahale20/NeoBank/master/architecture.png)

## ✨ Features

- **User Authentication & Authorization**: Secure login and registration using **JWT (JSON Web Tokens)** and **Spring Security**.
- **Role-Based Access Control (RBAC)**: Fine-grained access control based on user roles and authorities.
- **OTP-based Account Activation**: Added security layer requiring users to verify their accounts using One-Time Passwords (OTP).
- **Profile Management**: Secure REST APIs for users to view and update their profiles.
- **Password Recovery**: Secure password reset flow.
- **Robust Architecture**: Follows the **Controller-Service-Repository** layered architecture for clean separation of concerns.
- **Data Validation & Error Handling**: Comprehensive request validation and **Global Exception Handling** to provide meaningful API responses.
- **DTO Mapping**: Secure data transfer using Data Transfer Objects (DTOs) to abstract internal database entities from API endpoints.

## 🛠️ Technology Stack

- **Backend**: Java, Spring Boot, Spring Security
- **Database**: PostgreSQL
- **ORM**: Hibernate / Spring Data JPA
- **Build Tool**: Maven

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL database

### Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/nikhilmahale20/NeoBank.git
   cd NeoBank
   ```

2. **Configure Database:**
   Update the `src/main/resources/application.yml` or `application.properties` with your PostgreSQL database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/neobank
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build the project:**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

## 🏗️ Architecture

This microservice adopts a strict layered architecture:

- **Controllers**: Exposes RESTful endpoints, handles HTTP requests, and validates incoming data.
- **Services**: Contains the core business logic, orchestrating interactions between controllers and repositories.
- **Repositories**: Manages data persistence and retrieval from the PostgreSQL database using Spring Data JPA.
- **Security Layer**: Intercepts requests, validates JWTs, and enforces RBAC rules.
