# Contexte des contrats API

## Base path
Tous les endpoints sont sous `/api`.

## Students
Route de base: `/api/students`

| Method | Path | Success | Notes |
|---|---|---:|---|
| GET | `/api/students` | 200 | Retourne tous les students |
| GET | `/api/students/{id}` | 200 | Retourne un student ou 404 |
| POST | `/api/students` | 201 | Valide le body |
| PUT | `/api/students/{id}` | 200 | Met a jour un student existant ou 404 |
| DELETE | `/api/students/{id}` | 204 | Supprime par id |

Exemple de body:

```json
{
  "name": "John Smith",
  "email": "john@school.com",
  "enrollmentNumber": "MAT001"
}
```

## Rooms
Route de base: `/api/rooms`

| Method | Path | Success | Notes |
|---|---|---:|---|
| GET | `/api/rooms` | 200 | Retourne toutes les rooms |
| GET | `/api/rooms/{id}` | 200 | Retourne une room ou 404 |
| POST | `/api/rooms` | 201 | Valide le body |
| PUT | `/api/rooms/{id}` | 200 | Met a jour une room existante ou 404 |
| DELETE | `/api/rooms/{id}` | 204 | Supprime par id |

Exemple de body:

```json
{
  "name": "Computer Lab",
  "number": "101",
  "capacity": 30
}
```

## Teachers
Route de base: `/api/teachers`

| Method | Path | Success | Notes |
|---|---|---:|---|
| GET | `/api/teachers` | 200 | Retourne tous les teachers |
| GET | `/api/teachers/{id}` | 200 | Retourne un teacher ou 404 |
| POST | `/api/teachers` | 201 | Valide le body |
| PUT | `/api/teachers/{id}` | 200 | Met a jour un teacher existant ou 404 |
| DELETE | `/api/teachers/{id}` | 204 | Supprime par id |

Exemple de body:

```json
{
  "name": "Anne Costa",
  "email": "anne@school.com",
  "subject": "Mathematics"
}
```

## School schedule
Route de base: `/api/schedule`

| Method | Path | Success | Notes |
|---|---|---:|---|
| GET | `/api/schedule` | 200 | Retourne toutes les entrees de planning |
| GET | `/api/schedule/{id}` | 200 | Retourne une entree ou 404 |
| GET | `/api/schedule/date/{date}` | 200 | Format `date`: `yyyy-MM-dd` |
| GET | `/api/schedule/student/{studentId}` | 200 | Filtre par student |
| GET | `/api/schedule/teacher/{teacherId}` | 200 | Filtre par teacher |
| GET | `/api/schedule/room/{roomId}` | 200 | Filtre par room |
| POST | `/api/schedule` | 201 | Les entites liees doivent exister |
| PUT | `/api/schedule/{id}` | 200 | Met a jour une entree existante ou 404 |
| DELETE | `/api/schedule/{id}` | 204 | Supprime par id |

Exemple de body:

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

## Contrats d'erreur
### 404 Not Found
Retourne quand la ressource demandee n'existe pas.

Exemple de format:

```json
{
  "timestamp": "2026-05-06T10:15:30",
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with id: 999"
}
```

### 400 Validation Error
Retourne quand la validation Bean echoue.

Exemple de format:

```json
{
  "timestamp": "2026-05-06T10:15:30",
  "status": 400,
  "error": "Validation Error",
  "messages": [
    "email: must be a well-formed email address",
    "name: must not be blank"
  ]
}
```

### 400 Bad Request
Retourne quand `SchoolSchedule` reference des IDs d'entites invalides.

Exemple de format:

```json
{
  "timestamp": "2026-05-06T10:15:30",
  "status": 400,
  "error": "Bad Request",
  "message": "Teacher not found with id: 1"
}
```

## Guide Copilot
Lors de la generation de handlers ou de tests, conserver:
- les status HTTP ci-dessus
- les paths d'endpoint exactement comme implementes
- le comportement 400 base sur la validation
- la conversion cote controleur des resultats vides en 404
