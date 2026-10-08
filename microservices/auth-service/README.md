# HAMS Auth Service

Authentication and authorization microservice for the **Hospital Appointment Management System (HAMS)**.

The Auth Service is responsible for user accounts, roles, password hashing, authentication, JWT generation/validation, and account status.

It does **not** store doctor, patient, or appointment business information.

---

## 1. Responsibilities

The Auth Service is responsible for:

- User account management
- Username/password authentication
- Password hashing using BCrypt
- Role management
- JWT access-token generation
- JWT token validation
- Role-based authorization
- Account status management
- Current authenticated-user information
- Default system user initialization

### It does NOT manage

- Doctor profiles
- Patient profiles
- Appointments
- Doctor availability
- Hospital/business information

Those responsibilities belong to their respective microservices.

---

# 2. Technology Stack

| Technology | Version / Usage |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Security | Authentication & authorization |
| Spring Data JPA | Database access |
| PostgreSQL | 17 |
| Flyway | Database migrations |
| JWT | JJWT 0.12.7 |
| Maven | Build tool |
| Lombok | Boilerplate reduction |
| Spring Validation | Request validation |
| Spring Actuator | Health monitoring |

---

# 3. Microservice Architecture

```text
                         React UI
                            |
                            ▼
                       API Gateway
                            |
                            ▼
                    ┌───────────────┐
                    │ Auth Service  │
                    │    :8081      │
                    └───────┬───────┘
                            |
                            ▼
                     hams_auth_db
```

The complete HAMS architecture will eventually contain:

```text
React UI
    |
    ▼
API Gateway
    |
    ├── Auth Service       :8081
    ├── Doctor Service     :8082
    ├── Patient Service    :8083
    └── Appointment Service:8084
```

Each microservice owns its own database.

```text
Auth Service
    ↓
hams_auth_db

Doctor Service
    ↓
hams_doctor_db

Patient Service
    ↓
hams_patient_db

Appointment Service
    ↓
hams_appointment_db
```

---

# 4. Auth Service Responsibilities

The Auth Service owns authentication-related information.

```text
User
├── id
├── username
├── password
├── role
├── status
├── createdAt
└── updatedAt
```

The service does not contain:

```text
Doctor details
Patient details
Appointment details
```

For example, a doctor account is stored here:

```text
users
------------------------------------------------
id
username
password
role = DOCTOR
status = ACTIVE
```

The actual doctor profile will be stored in the Doctor Service.

---

# 5. User and Role Model

## Roles

The Auth Service currently supports:

```text
ADMIN
OPERATOR
PATIENT
DOCTOR
```

### ADMIN

System administrator.

Typical permissions:

- Manage doctors
- Manage users
- Approve doctors
- Access administrative functionality

### OPERATOR

Hospital operator.

Typical permissions:

- View/manage permitted hospital operations
- Access operator-specific functionality

### PATIENT

Hospital patient.

Patients will be able to self-register.

### DOCTOR

Hospital doctor.

Doctors may:

- Self-register
- Complete their doctor profile
- Access doctor-specific functionality after approval

---

# 6. User Status

User account status is managed by the Auth Service.

```text
ACTIVE
INACTIVE
```

### ACTIVE

The user can authenticate.

### INACTIVE

The user cannot authenticate.

The Auth Service does not use user status for doctor/patient profile workflow.

For example:

```text
Auth Service

UserStatus
----------------
ACTIVE
INACTIVE
```

Doctor-specific status will belong to the Doctor Service.

Patient-specific profile status will belong to the Patient Service.

---

# 7. Database

Database:

```text
hams_auth_db
```

PostgreSQL connection:

```text
localhost:5432
```

Example:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/hams_auth_db
    username: postgres
    password: YOUR_POSTGRES_PASSWORD
```

---

# 8. Database Schema

## roles

```text
roles
-------------------------
id
name
```

Example:

```text
ADMIN
OPERATOR
PATIENT
DOCTOR
```

## users

```text
users
-------------------------
id
username
password
role_id
status
created_at
updated_at
```

Relationship:

```text
users
   |
   | role_id
   ▼
roles
```

A user belongs to one role.

```text
User ──────── Many-to-One ──────── Role
```

---

# 9. Database Migrations

Flyway manages the database schema.

Migration files are located at:

```text
src/main/resources/db/migration/
```

Example:

```text
db/migration/
├── V1__create_users_and_roles.sql
├── V2__insert_default_roles.sql
└── V3__add_user_status.sql
```

The exact migration filenames should match the files currently present in the project.

The application uses:

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
```

Hibernate does not create or modify the schema.

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

This means:

> Flyway owns database schema changes. Hibernate only validates the entity/schema structure.

---

# 10. Prerequisites

Before running the Auth Service, install:

- Java 21
- Maven
- PostgreSQL 17

Verify Java:

```bash
java -version
```

Expected:

```text
Java 21
```

Verify Maven:

```bash
mvn -version
```

Verify PostgreSQL:

```bash
psql --version
```

---

# 11. Create the Database

Create the Auth Service database:

```sql
CREATE DATABASE hams_auth_db;
```

Verify:

```sql
SELECT datname
FROM pg_database
WHERE datname = 'hams_auth_db';
```

Flyway will create/update the required tables when the application starts.

---

# 12. Configure Database Connection

Open:

```text
src/main/resources/application.yml
```

Configure:

```yaml
spring:
  application:
    name: auth-service

  datasource:
    url: jdbc:postgresql://localhost:5432/hams_auth_db
    username: postgres
    password: YOUR_POSTGRES_PASSWORD
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
    properties:
      hibernate:
        format_sql: true

  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: 8081
```

Do not commit real database passwords to Git.

For production, use environment variables or a secret-management solution.

---

# 13. JWT Configuration

The Auth Service generates JWT access tokens.

Example configuration:

```yaml
jwt:
  secret: YOUR_BASE64_SECRET
  expiration: 3600000
```

`expiration` is in milliseconds.

```text
3600000 ms = 1 hour
```

The JWT secret must be a sufficiently strong Base64-encoded secret.

Do not commit production JWT secrets to Git.

Recommended production approach:

```text
Environment Variable
        ↓
JWT_SECRET
        ↓
Auth Service
```

---

# 14. Running the Service

Navigate to the Auth Service directory:

```bash
cd hams/microservices/auth-service
```

Run using Maven:

```bash
./mvnw spring-boot:run
```

Or:

```bash
mvn spring-boot:run
```

The service starts on:

```text
http://localhost:8081
```

---

# 15. Build the Application

Create a production JAR:

```bash
./mvnw clean package
```

Run the JAR:

```bash
java -jar target/*.jar
```

---

# 16. Health Check

Spring Boot Actuator exposes the health endpoint:

```http
GET /actuator/health
```

Example:

```bash
curl http://localhost:8081/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

---

# 17. Authentication Flow

The basic authentication flow is:

```text
Client
  |
  | username + password
  ▼
Auth Controller
  |
  ▼
Auth Service
  |
  ▼
AuthenticationManager
  |
  ▼
UserDetailsService
  |
  ▼
User Repository
  |
  ▼
PostgreSQL
```

If authentication succeeds:

```text
User
  ↓
JWT generated
  ↓
LoginResponse
  ↓
Client
```

---

# 18. Login

Endpoint:

```http
POST /api/v1/auth/login
```

Request:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Example response:

```json
{
  "accessToken": "<JWT>",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": "USER_UUID",
    "username": "admin",
    "role": "ADMIN"
  }
}
```

The access token must be sent in subsequent protected requests:

```http
Authorization: Bearer <JWT>
```

---

# 19. Current User

Endpoint:

```http
GET /api/v1/auth/me
```

Header:

```http
Authorization: Bearer <JWT>
```

Example response:

```json
{
  "id": "USER_UUID",
  "username": "admin",
  "role": "ADMIN"
}
```

The endpoint returns information about the currently authenticated user.

---

# 20. JWT Security

The Auth Service uses stateless JWT authentication.

```text
Client
  |
  | Login
  ▼
Auth Service
  |
  | JWT
  ▼
Client
  |
  | Authorization: Bearer JWT
  ▼
Protected API
```

The service uses:

```text
JwtAuthenticationFilter
```

to:

1. Read the Authorization header
2. Extract the Bearer token
3. Extract the username
4. Load the user
5. Validate the token
6. Create the Spring Security authentication
7. Store authentication in the SecurityContext

---

# 21. Authentication vs Authorization

Authentication answers:

> Who are you?

Authorization answers:

> What are you allowed to do?

Example:

```text
Authentication

admin + password
        ↓
JWT
```

Then authorization:

```text
JWT
 ↓
ROLE_ADMIN
 ↓
@PreAuthorize("hasRole('ADMIN')")
```

---

# 22. Role-Based Authorization

Method security is enabled using:

```java
@EnableMethodSecurity
```

Example:

```java
@PreAuthorize("hasRole('ADMIN')")
```

Only users with:

```text
ROLE_ADMIN
```

can access the method.

The `CustomUserDetailsService` converts the database role:

```text
ADMIN
```

into Spring Security authority:

```text
ROLE_ADMIN
```

---

# 23. HTTP Security Responses

The service distinguishes between authentication and authorization failures.

### No JWT

```text
HTTP 401 Unauthorized
```

Example:

```json
{
  "error": "Authentication required"
}
```

### Invalid/expired JWT

```text
HTTP 401 Unauthorized
```

### Valid JWT but insufficient permission

```text
HTTP 403 Forbidden
```

Example:

```json
{
  "error": "Access Denied"
}
```

Security handlers:

```text
JwtAuthenticationEntryPoint
        ↓
401 Unauthorized

JwtAccessDeniedHandler
        ↓
403 Forbidden
```

---

# 24. Password Security

Passwords are never stored as plain text.

The service uses:

```text
BCryptPasswordEncoder
```

Example:

```text
Original password:
admin123

Database:
$2a$10$................................
```

During login, Spring Security compares the supplied password against the BCrypt hash.

---

# 25. Default Users

The application initializes default users when required.

Current default accounts:

```text
ADMIN
--------------------------------
username: admin
password: admin123
role: ADMIN
status: ACTIVE
```

```text
OPERATOR
--------------------------------
username: operator
password: operator123
role: OPERATOR
status: ACTIVE
```

These are intended for development/testing.

Production credentials must be managed securely and should not use these default passwords.

---

# 26. Registration Design

Public registration must never allow the client to choose its role.

For example, this should NOT be accepted:

```json
{
  "username": "test",
  "password": "test123",
  "role": "ADMIN"
}
```

Role assignment is controlled by the server.

Patient registration:

```text
Register
   ↓
PATIENT role
```

Doctor registration:

```text
Register Doctor
   ↓
DOCTOR role
```

Admin/operator accounts are controlled by privileged system functionality.

---

# 27. Doctor Account Architecture

The Auth Service stores the doctor's authentication account:

```text
users
----------------------------------------
id
username
password
role = DOCTOR
status = ACTIVE
```

The Doctor Service will store the doctor's professional profile:

```text
doctors
----------------------------------------
id
user_id
first_name
last_name
specialization
qualification
experience
phone
...
```

The relationship is:

```text
Auth Service                     Doctor Service

users                            doctors
---------                         ---------
id  ────────────────────────────> user_id
username                           name
password                           specialization
role                               qualification
status                             experience
```

The Doctor Service will not store the doctor's password.

---

# 28. Doctor Registration Flows

The system will support two doctor-creation workflows.

## Doctor self-registration

```text
Doctor
  |
  | username + password
  ▼
Auth Service
  |
  | DOCTOR account
  ▼
Login
  |
  ▼
Doctor Service
  |
  | Complete profile
  ▼
Doctor Profile
```

## Admin creates doctor

```text
Admin
  |
  | username + password
  | + complete doctor details
  ▼
Doctor Service
  |
  | OpenFeign
  ▼
Auth Service
  |
  | create DOCTOR account
  ▼
userId
  |
  ▼
Doctor Service
  |
  ▼
Doctor profile
```

OpenFeign will be introduced for service-to-service communication.

---

# 29. Exception Handling

The service uses a global exception handler:

```text
GlobalExceptionHandler
```

Handled examples include:

```text
BadCredentialsException
DuplicateResourceException
RequiredRoleNotFoundException
```

Typical responses:

```text
Invalid credentials
→ 401 Unauthorized

Duplicate username
→ 409 Conflict

Unexpected server error
→ 500 Internal Server Error
```

Internal exception details should be logged but should not be exposed to clients.

---

# 30. Important Classes

The main package structure is approximately:

```text
com.hams.auth
│
├── controller
│   └── AuthController
│
├── dto
│   ├── LoginRequest
│   ├── LoginResponse
│   ├── RegisterRequest
│   └── UserResponse
│
├── entity
│   ├── User
│   ├── Role
│   └── UserStatus
│
├── repository
│   ├── UserRepository
│   └── RoleRepository
│
├── service
│   ├── AuthService
│   └── UserService
│
├── security
│   ├── SecurityConfig
│   ├── JwtService
│   ├── JwtAuthenticationFilter
│   ├── JwtAuthenticationEntryPoint
│   ├── JwtAccessDeniedHandler
│   └── CustomUserDetailsService
│
├── exception
│   ├── GlobalExceptionHandler
│   ├── DuplicateResourceException
│   └── RequiredRoleNotFoundException
│
└── config
    └── DataInitializer
```

The exact package structure may change as the service evolves.

---

# 31. Security Flow

```text
                    LOGIN
                      |
                      ▼
              AuthController
                      |
                      ▼
                AuthService
                      |
                      ▼
          AuthenticationManager
                      |
                      ▼
       CustomUserDetailsService
                      |
                      ▼
                 UserRepository
                      |
                      ▼
                PostgreSQL
                      |
                      ▼
                 JwtService
                      |
                      ▼
                    JWT
```

For protected requests:

```text
Client
  |
  | Authorization: Bearer JWT
  ▼
JwtAuthenticationFilter
  |
  ▼
JwtService
  |
  ▼
CustomUserDetailsService
  |
  ▼
SecurityContext
  |
  ▼
Controller
  |
  ▼
@PreAuthorize
```

---

# 32. Testing With Postman

Recommended testing sequence:

### 1. Health

```http
GET http://localhost:8081/actuator/health
```

### 2. Login as ADMIN

```http
POST http://localhost:8081/api/v1/auth/login
```

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Copy the returned JWT.

### 3. Test `/me`

```http
GET http://localhost:8081/api/v1/auth/me
```

Header:

```text
Authorization: Bearer <JWT>
```

### 4. Test ADMIN endpoint

```http
GET http://localhost:8081/api/v1/test/admin
```

Expected:

```text
200 OK
```

### 5. Login as OPERATOR

```json
{
  "username": "operator",
  "password": "operator123"
}
```

Use the operator JWT against the ADMIN endpoint.

Expected:

```text
403 Forbidden
```

### 6. Request protected endpoint without JWT

Expected:

```text
401 Unauthorized
```

---

# 33. Running Tests

Run:

```bash
./mvnw test
```

Or:

```bash
mvn test
```

Tests should cover:

- Login success
- Invalid password
- Unknown username
- JWT generation
- JWT validation
- Expired JWT
- `/me`
- Missing JWT
- Invalid JWT
- ADMIN authorization
- OPERATOR authorization
- Inactive account
- Duplicate username
- Validation errors

---

# 34. Git Configuration

Do not commit secrets such as:

```text
PostgreSQL password
JWT secret
Production credentials
API keys
```

Use environment variables for sensitive configuration.

Example:

```yaml
spring:
  datasource:
    password: ${DB_PASSWORD}

jwt:
  secret: ${JWT_SECRET}
```

Then configure:

```bash
export DB_PASSWORD=your-password
export JWT_SECRET=your-secret
```

---

# 35. Current Development Status

The Auth Service currently provides:

- [x] Spring Boot project
- [x] PostgreSQL connection
- [x] Flyway migrations
- [x] User entity
- [x] Role entity
- [x] User repository
- [x] Role repository
- [x] ADMIN role
- [x] OPERATOR role
- [x] PATIENT role
- [x] DOCTOR role
- [x] Default ADMIN user
- [x] Default OPERATOR user
- [x] BCrypt password hashing
- [x] Spring Security
- [x] AuthenticationManager
- [x] Custom UserDetailsService
- [x] JWT generation
- [x] JWT validation
- [x] JWT authentication filter
- [x] Stateless authentication
- [x] `/auth/login`
- [x] `/auth/me`
- [x] Role-based authorization
- [x] 401 AuthenticationEntryPoint
- [x] 403 AccessDeniedHandler
- [x] Global exception handling
- [x] UserStatus: ACTIVE / INACTIVE

---

# 36. Next Development Steps

The planned microservice development sequence is:

```text
AUTH SERVICE
    │
    ├── Complete registration APIs
    │
    ├── Tests
    │
    ▼
DOCTOR SERVICE
    │
    ├── Doctor entity
    ├── Doctor CRUD
    ├── /doctors/me
    ├── Doctor self-registration flow
    └── Admin doctor creation
    │
    ▼
OPENFEIGN
    │
    └── Doctor Service → Auth Service
    │
    ▼
APPOINTMENT SERVICE
    │
    ▼
SERVICE-TO-SERVICE COMMUNICATION
    │
    ▼
API GATEWAY
    │
    ▼
JWT VALIDATION ACROSS SERVICES
    │
    ▼
RESILIENCE4J
    │
    ▼
KAFKA
    │
    ▼
TRANSACTIONAL OUTBOX
    │
    ▼
DOCKER COMPOSE
    │
    ▼
CI/CD
    │
    ▼
AWS
```

---

# 37. Important Architectural Principle

The Auth Service answers:

> **Who are you?**

The Doctor Service answers:

> **What is your doctor profile?**

The Patient Service answers:

> **What is your patient profile?**

The Appointment Service answers:

> **What appointments exist?**

Each service owns its data and communicates with other services through APIs/events rather than directly accessing another service's database.

---

## Service Information

```text
Service Name: auth-service

Port:
8081

Database:
hams_auth_db

Base API:
http://localhost:8081/api/v1

Health:
http://localhost:8081/actuator/health
```

The Auth Service is the authentication foundation for the HAMS microservices architecture.