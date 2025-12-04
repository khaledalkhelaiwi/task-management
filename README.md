# Task Management – Spring Boot (JWT + Roles + H2)

A compact backend that provides user registration/login with JWT, role‑based authorization (USER/ADMIN), and full CRUD for tasks. Uses in‑memory H2 DB for quick testing.

## ✅ Features
- Register users with **BCrypt** password hashing
- Login and receive a **JWT** token
- Protect REST APIs with **JWT**
- **Role-based Access**:
  - USER → can only see their own tasks (`/api/tasks/my`)
  - ADMIN → can see all tasks (`/api/tasks/all`)
- Full **CRUD** on tasks
- **H2 Console** for inspecting data during development

---

## 📦 Stack
- Java 21
- Spring Boot 3.5.8
- Spring Data JPA (Hibernate)
- Spring Security 6 (JWT)
- H2 Database (in‑memory)
- Maven

> Note: Using `io.jsonwebtoken:jjwt:0.9.1` + `jakarta.xml.bind:jakarta.xml.bind-api` to avoid `DatatypeConverter` issues on modern Java.

---

## 🗂️ Project Structure
```
com.task_management_mini_system.task_management
│
├── TaskManagementApplication.java
│
├── config
│   ├── SecurityConfig.java        # Security Filter Chain + AuthenticationProvider
│   ├── JwtFilter.java             # Extract/validate JWT from requests
│   └── JwtUtil.java               # Generate/parse/validate JWT
│
├── controller
│   ├── AuthController.java        # /api/auth/register , /api/auth/login
│   └── TaskController.java        # /api/tasks/** (CRUD + my/all)
│
├── model
│   ├── User.java                  # username,email,password,role + tasks
│   ├── Task.java                  # title,desc,status,createdAt,user
│   └── enums
│       ├── Role.java              # ROLE_USER , ROLE_ADMIN
│       └── TaskStatus.java        # NEW, IN_PROGRESS, DONE
│
├── repository
│   ├── UserRepository.java
│   └── TaskRepository.java
│
├── service
│   ├── UserService.java           # UserDetailsService + register + CRUD
│   └── TasksService.java          # Task logic + user binding
│
└── exception (optional)
    ├── ResourceNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## ⚙️ Setup & Run

### Prereqs
- JDK 21
- Maven 3.9+

### Run
```bash
mvn spring-boot:run
```

Defaults:
- Server: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:taskdb`
  - User: `sa`
  - Password: (empty)

---

## 🔐 Auth & Roles

### Register
**POST** `/api/auth/register`

Body:
```json
{
  "username": "khaled",
  "email": "khaled@test.com",
  "password": "12345"
}
```

> Password is stored **BCrypt‑hashed**.

### Login (get JWT)
**POST** `/api/auth/login`

Body:
```json
{
  "username": "khaled",
  "password": "12345"
}
```

Response:
```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

Add the token in every protected request:
```
Authorization: Bearer <TOKEN>
```

---

## 🧪 Task Endpoints

> All require JWT.  
> `GET /api/tasks/all` is **ADMIN‑only**.

### 1) Create Task
**POST** `/api/tasks`

- **USER**: do **not** send `user`; the task will be assigned automatically to the token owner.
- **ADMIN**: may assign to a user by sending `user.id`.

Example (ADMIN assigning to user id=2):
```json
{
  "title": "Review PRs",
  "description": "Check pending pull requests",
  "status": "NEW",
  "createdAt": "2025-12-04",
  "user": { "id": 2 }
}
```

### 2) Get My Tasks
**GET** `/api/tasks/my`  
Returns tasks for the **current token user** (e.g., *khaled*).

### 3) Get All Tasks (ADMIN)
**GET** `/api/tasks/all`  
Returns **all tasks** in the system.

### 4) Update Task
**PUT** `/api/tasks/{id}`

Body:
```json
{
  "title": "Review PRs (updated)",
  "description": "Check PRs again",
  "status": "IN_PROGRESS",
  "createdAt": "2025-12-04"
}
```

- USER can only update **their own** tasks.
- ADMIN can update **any** task (subject to your service logic).

### 5) Delete Task
**DELETE** `/api/tasks/{id}`

- USER can only delete **their own** tasks.
- ADMIN can delete **any** task.

---

## 🧰 Quick cURL

### Register user *khaled*
```bash
curl -X POST http://localhost:8080/api/auth/register  -H "Content-Type: application/json"  -d '{"username":"khaled","email":"khaled@test.com","password":"12345"}'
```

### Login (get token)
```bash
curl -X POST http://localhost:8080/api/auth/login  -H "Content-Type: application/json"  -d '{"username":"khaled","password":"12345"}'
```

### Create task (as USER – auto‑assign to *khaled*)
```bash
curl -X POST http://localhost:8080/api/tasks  -H "Authorization: Bearer <TOKEN>"  -H "Content-Type: application/json"  -d '{"title":"Study Java","description":"Watch tutorials","status":"NEW","createdAt":"2025-12-04"}'
```

### Create task for user id=2 (ADMIN)
```bash
curl -X POST http://localhost:8080/api/tasks  -H "Authorization: Bearer <ADMIN_TOKEN>"  -H "Content-Type: application/json"  -d '{"title":"Assign Task","description":"To user 2","status":"NEW","createdAt":"2025-12-04","user":{"id":2}}'
```

### Get my tasks
```bash
curl -H "Authorization: Bearer <TOKEN>" http://localhost:8080/api/tasks/my
```

### Get all tasks (ADMIN)
```bash
curl -H "Authorization: Bearer <ADMIN_TOKEN>" http://localhost:8080/api/tasks/all
```

---

## 🧠 Notes
- **H2 in‑memory**: data resets on restart. For persistence, switch to MySQL/PostgreSQL in `application.properties`.
- **Assignment logic**:
  - USER: don’t send `user` in POST; the service assigns the task to the token owner.
  - ADMIN: allowed to assign tasks by sending `user.id`.
- **SecurityConfig**:
  - `/api/auth/**` and `/h2-console/**` → `permitAll`
  - `/api/tasks/**` → `authenticated`
  - Service layer enforces role logic for **my** and **all** routes.

---

## 🛠️ Troubleshooting
- **401 Unauthorized**: Missing/invalid `Authorization: Bearer <TOKEN>` header.
- **403 Forbidden**: Not enough privileges (e.g., USER calling `/api/tasks/all`) or authentication failed earlier.
- **NoClassDefFoundError: javax/xml/bind/DatatypeConverter** → add:
  ```xml
  <dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>2.3.3</version>
  </dependency>
  ```
- Ensure user creation/updates go through `UserService` to hash passwords.

---

## 🔄 Switch DB (optional)
Example MySQL config in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
spring.datasource.username=root
spring.datasource.password=yourpass
spring.jpa.hibernate.ddl-auto=update
```

---

## 📝 Deliverables
- Postman collection (Register/Login/Tasks CRUD)
- Screenshots of successful Postman calls
- This README
- (Optional) `data.sql` seed file

---

## 💡 Future Work
- Simple UI (JSP or React) to consume APIs
- Pagination/Sorting
- Refresh Tokens
- Task filtering by status/date
