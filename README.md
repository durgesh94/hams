# Hospital Appointment Management System

Hospital appointment management application with a Spring Boot REST API and a React + TypeScript frontend. The system provides JWT-based authentication and workflows for managing doctors, patients, appointments, and monthly dashboard statistics.

## Features

- JWT login and authenticated API requests
- Doctor management with filtering by gender, specialization, and status
- Patient management
- Appointment creation, updates, status changes, filtering, and deletion
- Monthly dashboard statistics for active doctors, new patients, and appointments
- PostgreSQL persistence with Flyway database migrations
- Unit, MVC, and JPA specification tests

## Technology Stack

### API

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA and Hibernate
- Spring Security
- PostgreSQL
- Flyway
- JSON Web Tokens with JJWT
- Maven

### UI

- React 19
- TypeScript 6
- Vite
- Material UI
- Redux Toolkit and RTK Query
- React Router
- React Hook Form and Zod
- pnpm

## Prerequisites

Install the following before starting the project:

- Java 21
- PostgreSQL
- Node.js compatible with the frontend toolchain
- pnpm

Create an empty PostgreSQL database. Flyway creates and updates the application tables when the API starts.

```sql
CREATE DATABASE hospital_appointment_db;
```

## Configuration

### API environment variables

The API uses the following environment variables. The values shown are the application defaults, except for the database password, which should be supplied in every non-local environment.

| Variable | Default | Description |
| --- | --- | --- |
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `hospital_appointment_db` | Database name |
| `DB_USERNAME` | `postgres` | Database user |
| `DB_PASSWORD` | empty | Database password |
| `SERVER_PORT` | `8080` | API port |
| `JWT_SECRET` | development value in `application.yml` | JWT signing secret |
| `JWT_EXP` | `3600000` | JWT lifetime in milliseconds |

Example API configuration for a local PostgreSQL instance:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=hospital_appointment_db
export DB_USERNAME=postgres
export DB_PASSWORD=your-postgres-password
export JWT_SECRET=replace-with-a-long-random-secret
```

### UI environment variables

Create `ui/.env.local`:

```dotenv
VITE_API_BASE_URL=http://localhost:8080
```

The frontend sends the JWT returned by the login endpoint as a Bearer token on authenticated requests.

## Running Locally

Start the API in one terminal:

```bash
cd api
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run` instead.

Start the UI in a second terminal:

```bash
cd ui
pnpm install
pnpm dev
```

The default local URLs are:

- UI: `http://localhost:5173`
- API: `http://localhost:8080`

Open the UI at `http://localhost:5173/login`.

## API Routes

All business routes are prefixed with `/api/v1`.

| Area | Routes |
| --- | --- |
| Health | `GET /health` |
| Authentication | `POST /auth/login`, `GET /auth/me` |
| Doctors | `GET /doctors`, `GET /doctors/{id}`, `POST /doctors`, `PUT /doctors/{id}`, `DELETE /doctors/{id}`, `GET /doctors/filter` |
| Patients | `GET /patients`, `GET /patients/{id}`, `POST /patients`, `PUT /patients/{id}`, `DELETE /patients/{id}` |
| Appointments | `GET /appointments`, `GET /appointments/{id}`, `POST /appointments`, `PUT /appointments/{id}`, `PATCH /appointments/{id}/status`, `DELETE /appointments/{id}`, `GET /appointments/filter` |
| Dashboard | `GET /dashboard/month?month=YYYY-MM` |

Except for the health and login endpoints, requests require an authenticated user and a Bearer token.

## Testing and Quality Checks

Run the API test suite and generate the JaCoCo report:

```bash
cd api
./mvnw clean verify
```

The coverage report is generated at `api/target/site/jacoco/index.html`.

Run an individual API test class:

```bash
cd api
./mvnw -Dtest=DashboardServiceTest test
```

Run the UI checks:

```bash
cd ui
pnpm lint
pnpm build
```

## Project Structure

```text
.
├── api/
│   ├── src/main/java/                  Spring Boot application code
│   ├── src/main/resources/db/migration Flyway migrations
│   └── src/test/java/                  API unit, MVC, and JPA tests
├── ui/
│   ├── src/features/                   RTK Query APIs and feature state
│   ├── src/pages/                      Dashboard, doctor, patient, and appointment pages
│   ├── src/components/                 Shared and feature UI components
│   └── src/routes/                     Public and protected routes
└── _notes/                             Project notes and supporting material
```

## Database Migrations

Flyway migrations are stored in `api/src/main/resources/db/migration` and run automatically on API startup. Current migrations create users, roles, doctors, patients, appointments, and audit columns.

Do not edit an already-applied migration. Add a new versioned migration for schema changes.

## Troubleshooting

### API cannot connect to PostgreSQL

Confirm PostgreSQL is running, the database exists, and `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD` match the local database.

### UI requests fail with a network error

Confirm the API is running on port `8080` and that `ui/.env.local` contains the correct `VITE_API_BASE_URL`. Restart Vite after changing environment variables.

### JWT requests return `401`

Log in through the UI or call `POST /api/v1/auth/login`, then send the returned token as:

```http
Authorization: Bearer <token>
```
