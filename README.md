

````
# Task Management Mini System

Mini backend project to manage **users** and **tasks** using **Java**, **Spring Boot**, and **JWT**.

This project is a training mini system that demonstrates how to build secure REST APIs with authentication, authorization, role-based access, and PDF reporting using **JasperReports**.

---

## 1) Technologies Used

- Java
- Spring Boot
- Spring Web (REST APIs)
- Spring Data JPA (Hibernate)
- Spring Security
- JWT (JSON Web Token)
- H2 Database (in-memory)
- Maven
- JasperReports (PDF generation)

---

## 2) Project Structure (Layers)

The project follows a clean layered architecture:

- `controller`
  - `AuthController` – handles user registration and login
  - `TaskController` – handles CRUD operations for tasks
  - `ReportController` – handles PDF report generation

- `service`
  - `UserService` – user registration and authentication logic
  - `TasksService` – business logic for task operations
  - `ReportService` – JasperReports PDF generation logic

- `repository`
  - `UserRepository` – repository for `User` entity
  - `TaskRepository` – repository for `Task` entity

- `model`
  - `User` – represents application users
  - `Task` – represents tasks
  - `TaskStatus` – enum (`NEW`, `IN_PROGRESS`, `DONE`)
  - `Role` – enum (`ROLE_USER`, `ROLE_ADMIN`)

- `report`
  - `dto/TaskReportRow` – DTO used for JasperReports field mapping

- `config`
  - `SecurityConfig` – Spring Security configuration
  - `JwtFilter` – extracts and validates JWT tokens
  - `JwtUtil` – generates and parses JWT tokens

---

## 3) Database Configuration

The project uses **H2 in-memory database** for simplicity.

Default configuration:

```properties
spring.datasource.url=jdbc:h2:mem:taskdb
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
````

H2 Console:

```
http://localhost:8080/h2-console
```

---

## 4) Report Configuration (JasperReports)

### 4.1 Template Location

Jasper report templates are stored inside the backend resources:

```
src/main/resources/reports/
```

Example:

* `tasks_report.jrxml`

### 4.2 Report Output

Reports are generated dynamically as **PDF files** and returned directly from the backend as downloadable responses.

---

## 5) How to Run the Project

### Prerequisites

* JDK
* Maven

### Run from Command Line

```bash
mvn spring-boot:run
```

The application will start on:

```
http://localhost:8080
```

---

## 6) Authentication & Security

### JWT Authentication Flow

1. User registers or logs in.
2. Backend returns a JWT token.
3. The token must be sent with every protected request:

```http
Authorization: Bearer <TOKEN>
```

### Password Security

User passwords are encrypted using **BCryptPasswordEncoder** before being stored.

### Role-Based Authorization

* `USER`:

  * Can access only their own tasks
  * Can generate PDF report for their own tasks

* `ADMIN`:

  * Can access all tasks
  * Can generate PDF report for all tasks

---

## 7) REST API Endpoints

### 7.1 Authentication APIs

#### Register User

**POST** `/api/auth/register`

```json
{
  "username": "user1",
  "email": "user1@test.com",
  "password": "1234"
}
```

#### Login

**POST** `/api/auth/login`

```json
{
  "username": "user1",
  "password": "1234"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### 7.2 Task APIs (Protected)

All endpoints require JWT authentication.

#### Create Task

**POST** `/api/tasks`

```json
{
  "title": "Finish mini project",
  "description": "Complete Task Management Mini System",
  "status": "NEW"
}
```

#### Get My Tasks

**GET** `/api/tasks/my`

Returns tasks belonging to the authenticated user.

#### Get All Tasks (ADMIN only)

**GET** `/api/tasks/all`

Returns all tasks in the system.

#### Update Task

**PUT** `/api/tasks/{id}`

```json
{
  "title": "Updated title",
  "description": "Updated description",
  "status": "IN_PROGRESS"
}
```

#### Delete Task

**DELETE** `/api/tasks/{id}`

---

### 7.3 Report APIs (Protected)

#### My Tasks PDF Report (USER)

**GET** `/api/reports/tasks/my`

* Requires JWT
* Returns a downloadable **PDF** file containing the user's tasks

#### All Tasks PDF Report (ADMIN only)

**GET** `/api/reports/tasks/all`

* Requires `ADMIN` role
* Returns a downloadable **PDF** file containing all tasks

---

## 8) JasperReports Field Mapping

The report template uses the following fields, which must match the DTO:

* `id`
* `title`
* `description`
* `status`

These fields are mapped from `TaskReportRow` in the backend.

---

## 9) Postman Testing

Recommended testing flow using Postman:

1. Register user
2. Login and copy JWT token
3. Test Task APIs
4. Test Report APIs using **Send and Download** to save the PDF file

---

## 10) Notes

* The H2 database is **in-memory**, so data will be reset on application restart.
* PDF reports are generated dynamically using JasperReports.
* Security rules are enforced using Spring Security and JWT.

---

## 11) Future Improvements

* Pagination and sorting for tasks
* Filtering by task status
* Excel (XLSX) report support
* Frontend integration (Angular / React)

```

---
👌
```
