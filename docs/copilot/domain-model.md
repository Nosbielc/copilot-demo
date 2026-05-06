# Contexte du modele de domaine

## Vue d'ensemble
Le systeme gere un domaine scolaire simple avec quatre entites principales:
- `Student`
- `Teacher`
- `Room`
- `SchoolSchedule`

## `Student`
Table: `students`

Champs:
- `id: Long` - cle primaire generee
- `name: String` - requis (`@NotBlank`)
- `email: String` - requis, email valide, unique (`@NotBlank`, `@Email`, `unique = true`)
- `enrollmentNumber: String` - requis, unique (`@NotBlank`, `unique = true`)

Methodes repository deja disponibles:
- `findByEmail`
- `findByEnrollmentNumber`
- `existsByEmail`
- `existsByEnrollmentNumber`

## `Teacher`
Table: `teachers`

Champs:
- `id: Long` - cle primaire generee
- `name: String` - requis
- `email: String` - requis, email valide, unique
- `subject: String` - requis

Methodes repository deja disponibles:
- `findByEmail`
- `existsByEmail`

## `Room`
Table: `rooms`

Champs:
- `id: Long` - cle primaire generee
- `name: String` - requis
- `number: String` - requis, unique
- `capacity: Integer` - requis, valeur minimale 1

Methodes repository deja disponibles:
- `findByNumber`
- `existsByNumber`

## `SchoolSchedule`
Table: `school_schedule`

Champs:
- `id: Long` - cle primaire generee
- `date: LocalDate` - requis
- `time: LocalTime` - requis
- `description: String` - requis
- `student: Student` - `@ManyToOne(fetch = EAGER)` requis
- `teacher: Teacher` - `@ManyToOne(fetch = EAGER)` requis
- `room: Room` - `@ManyToOne(fetch = EAGER)` requis

Colonnes de jointure:
- `student_id`
- `teacher_id`
- `room_id`

Methodes repository deja disponibles:
- `findByDate`
- `findByStudentId`
- `findByTeacherId`
- `findByRoomId`

## Regles de relation
- Une entree de planning doit referencer des enregistrements `Student`, `Teacher` et `Room` existants.
- En save/update, le service charge les entites referencees depuis leurs repositories avant de sauvegarder le planning.
- Si une entite referencee n'existe pas, l'API retourne actuellement HTTP 400 via `IllegalArgumentException`.

## Conventions de payload
Pour les requetes create/update de `SchoolSchedule`, l'API attend des objets imbriques contenant au minimum les valeurs `id` des relations. Exemple:

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

## Contraintes a preserver avec Copilot
- Conserver les annotations de validation sur les entites.
- Conserver les cles metier uniques:
  - `Student.email`
  - `Student.enrollmentNumber`
  - `Teacher.email`
  - `Room.number`
- Ne pas remplacer les references d'entites dans `SchoolSchedule` par de simples champs d'ID, sauf demande explicite de refactor DTO.
- Si de nouveaux champs sont ajoutes, mettre a jour validation, tests et exemples API de maniere coherente.
