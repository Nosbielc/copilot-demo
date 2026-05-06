# copilot-demo

Spring Boot REST API with reactive endpoints, JPA, HikariCP, and an in-memory H2 database.

## Features

- **Student CRUD** — `GET/POST/PUT/DELETE /api/students`
- **Room CRUD** — `GET/POST/PUT/DELETE /api/rooms`
- **Teacher CRUD** — `GET/POST/PUT/DELETE /api/teachers`
- **School Schedule CRUD** — `GET/POST/PUT/DELETE /api/schedule`

## Technologies

| Technology | Description |
|---|---|
| Spring Boot 3.2 | Main framework |
| Spring WebFlux | Reactive endpoints (Mono/Flux) |
| Spring Data JPA | Data access with Hibernate |
| HikariCP | Connection pool |
| H2 Database | In-memory database |
| Jakarta Validation | Input validation |

## How to Run

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.  
The H2 console will be available at `http://localhost:8080/h2-console`
(JDBC URL: `jdbc:h2:mem:escola`, user: `sa`, password: blank).

## Endpoints

### Students

| Method | URL | Description |
|---|---|---|
| GET | `/api/students` | List all students |
| GET | `/api/students/{id}` | Get student by ID |
| POST | `/api/students` | Create new student |
| PUT | `/api/students/{id}` | Update student |
| DELETE | `/api/students/{id}` | Delete student |

**Example payload:**
```json
{
  "name": "John Smith",
  "email": "john@school.com",
  "enrollmentNumber": "MAT001"
}
```

### Rooms

| Method | URL | Description |
|---|---|---|
| GET | `/api/rooms` | List all rooms |
| GET | `/api/rooms/{id}` | Get room by ID |
| POST | `/api/rooms` | Create new room |
| PUT | `/api/rooms/{id}` | Update room |
| DELETE | `/api/rooms/{id}` | Delete room |

**Example payload:**
```json
{
  "name": "Computer Lab",
  "number": "101",
  "capacity": 30
}
```

### Teachers

| Method | URL | Description |
|---|---|---|
| GET | `/api/teachers` | List all teachers |
| GET | `/api/teachers/{id}` | Get teacher by ID |
| POST | `/api/teachers` | Create new teacher |
| PUT | `/api/teachers/{id}` | Update teacher |
| DELETE | `/api/teachers/{id}` | Delete teacher |

**Example payload:**
```json
{
  "name": "Anne Costa",
  "email": "anne@school.com",
  "subject": "Mathematics"
}
```

### School Schedule

| Method | URL | Description |
|---|---|---|
| GET | `/api/schedule` | List all schedule entries |
| GET | `/api/schedule/{id}` | Get schedule entry by ID |
| GET | `/api/schedule/date/{date}` | Get schedule entries by date (yyyy-MM-dd) |
| GET | `/api/schedule/student/{studentId}` | Get schedule entries by student |
| GET | `/api/schedule/teacher/{teacherId}` | Get schedule entries by teacher |
| GET | `/api/schedule/room/{roomId}` | Get schedule entries by room |
| POST | `/api/schedule` | Create new schedule entry |
| PUT | `/api/schedule/{id}` | Update schedule entry |
| DELETE | `/api/schedule/{id}` | Delete schedule entry |

**Example payload:**
```json
{
  "date": "2024-06-15",
  "time": "08:00:00",
  "description": "Mathematics class",
  "student": { "id": 1 },
  "teacher": { "id": 1 },
  "room": { "id": 1 }
}
```

## Running Tests

```bash
mvn test
```

