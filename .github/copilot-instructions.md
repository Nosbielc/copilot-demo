# Instructions Copilot pour `copilot-demo`

## Vue d'ensemble du projet
- Stack: Java 17, Spring Boot 3.2.5, Maven.
- Style d'API: controleurs REST utilisant les types de retour Spring WebFlux (`Mono` et `Flux`).
- Persistance: Spring Data JPA avec des repositories bloquants (`JpaRepository`) et base H2 en memoire.
- Validation: annotations Jakarta Validation sur les entites + `@Valid` dans les controleurs.
- Gestion d'erreurs: `ResourceNotFoundException` pour les 404 et `GlobalExceptionHandler` pour les reponses 400/404.

## Regles d'architecture
- Conserver la structure de packages existante:
  - `controller` pour les endpoints HTTP
  - `service` pour la logique metier
  - `repository` pour l'acces JPA
  - `entity` pour les modeles JPA
- Utiliser uniquement l'injection par constructeur.
- Garder les controleurs legers. La logique metier doit rester dans les services.
- Garder les repositories comme interfaces Spring Data JPA sauf si une vraie requete personnalisee est necessaire.

## Pattern reactif + bloquant
Ce projet utilise des endpoints reactifs au-dessus de repositories JPA bloquants.

Lors de l'ajout ou de la modification de methodes de service:
- Encapsuler les appels bloquants repository avec `Mono.fromCallable(...)` ou `Mono.fromRunnable(...)`.
- Utiliser `Flux::fromIterable` pour retourner des collections.
- Deporter le travail bloquant avec `subscribeOn(Schedulers.boundedElastic())`.
- Preserver le pattern deja utilise dans `StudentService`, `TeacherService`, `RoomService` et `SchoolScheduleService`.

## Conventions CRUD
- `GET /resource` retourne tous les elements.
- `GET /resource/{id}` retourne un element ou 404.
- `POST /resource` retourne `201 Created`.
- `PUT /resource/{id}` retourne l'element mis a jour ou 404.
- `DELETE /resource/{id}` retourne `204 No Content`.
- Les controleurs doivent appeler `.switchIfEmpty(Mono.error(new ResourceNotFoundException(...)))` pour les ressources absentes.

## Validation et regles metier
- `Student`
  - `name`: requis
  - `email`: requis, email valide, unique
  - `enrollmentNumber`: requis, unique
- `Teacher`
  - `name`: requis
  - `email`: requis, email valide, unique
  - `subject`: requis
- `Room`
  - `name`: requis
  - `number`: requis, unique
  - `capacity`: requis, minimum 1
- `SchoolSchedule`
  - `date`, `time`, `description`: requis
  - `student`, `teacher`, `room`: entites existantes requises, referencees par `id`

## Conventions de reponse d'erreur
- Les erreurs de validation retournent HTTP 400 avec:
  - `timestamp`
  - `status`
  - `error`
  - `messages` (liste des messages de validation par champ)
- Ressource absente retourne HTTP 404 avec:
  - `timestamp`
  - `status`
  - `error`
  - `message`
- Les references invalides d'entites liees dans les operations de planning retournent HTTP 400 via `IllegalArgumentException`.

## Guide de test
- Les tests existants dans `src/test/java/com/example/demo/CopilotDemoApplicationTests.java` sont des tests d'integration avec `@SpringBootTest` et `WebTestClient`.
- Pour les tests unitaires, privilegier JUnit 5 + Mockito via `spring-boot-starter-test`.
- Pour les tests unitaires de service:
  - mocker les repositories
  - utiliser `StepVerifier` pour `Mono`/`Flux`
  - verifier les interactions repository avec Mockito
- Pour les tests d'integration des controleurs, continuer a utiliser `WebTestClient`.

## Lors des changements
Quand vous ajoutez ou modifiez une fonctionnalite, mettez a jour les couches concernees de facon coherente:
1. `entity` si le modele metier change
2. `repository` si une nouvelle requete est necessaire
3. `service` pour la logique
4. `controller` pour l'endpoint
5. tests dans `src/test/java/...`
6. documentation si l'API publique change

## Fichiers de contexte supplementaires
Utiliser ces fichiers comme contexte de support:
- `docs/copilot/architecture.md`
- `docs/copilot/domain-model.md`
- `docs/copilot/api-contracts.md`
- `docs/copilot/testing.md`
