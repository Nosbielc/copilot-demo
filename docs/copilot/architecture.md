# Contexte d'architecture

## Structure globale
L'application suit une architecture en couches:

```text
Requete HTTP
  -> controller
  -> service
  -> repository
  -> base H2 via JPA/Hibernate
```

Packages sous `src/main/java/com/example/demo/`:
- `controller`
- `service`
- `repository`
- `entity`

## Caracteristique architecturale cle
Ce projet combine:
- **endpoints HTTP reactifs** avec Spring WebFlux (`Mono`, `Flux`)
- **persistance bloquante** avec Spring Data JPA (`JpaRepository`)

Comme JPA est bloquant, les methodes de service doivent encapsuler l'acces repository dans des publishers Reactor et deplacer le travail vers `Schedulers.boundedElastic()`.

## Pattern d'implementation actuel
### Controleurs
Les controleurs:
- exposent des endpoints REST sous `/api/...`
- retournent `Mono<T>`, `Flux<T>` ou `Mono<Void>`
- valident les payloads avec `@Valid`
- transforment les resultats absents en `ResourceNotFoundException`

Pattern type:
- `findById(id)` dans le controleur appelle le service
- si le service retourne vide, le controleur convertit en 404 avec `.switchIfEmpty(...)`

### Services
Les services:
- contiennent la logique applicative
- appellent les repositories
- encapsulent les appels bloquants avec `Mono.fromCallable(...)`, `Mono.fromRunnable(...)` et `Flux::fromIterable`
- marquent les operations d'ecriture avec `@Transactional`

Conventions actuelles dans les services:
- `findAll()` => `Mono.fromCallable(repository::findAll).flatMapMany(Flux::fromIterable)`
- `findById(id)` => `Mono.fromCallable(() -> repository.findById(id)).flatMap(...)`
- `save(entity)` => `Mono.fromCallable(() -> repository.save(entity))`
- `delete(id)` => `Mono.fromRunnable(() -> repository.deleteById(id)).then()`

### Repositories
Les repositories sont des interfaces Spring Data JPA simples qui etendent `JpaRepository`.
Preferer les derived queries avant d'introduire du JPQL personnalise.

### Entites
Les entites sont des modeles JPA avec annotations Jakarta Validation.
Il n'y a pas de couche DTO pour le moment; les requetes/reponses utilisent directement les entites.

## Flux specifique au domaine
### Entites CRUD de base
`Student`, `Teacher` et `Room` suivent le meme flux CRUD:
- le controleur delegue au service
- le service delegue au repository
- entite absente en lecture/mise a jour => publisher vide dans le service puis 404 dans le controleur

### `SchoolSchedule`
`SchoolSchedule` est le seul agregat avec relations.
Avant save/update, le service resout et valide les `Student`, `Teacher` et `Room` references via leur `id`.
Si l'une d'elles n'existe pas, le service leve `IllegalArgumentException`, convertie en HTTP 400.

## Gestion d'erreurs
La gestion d'erreurs est centralisee dans `GlobalExceptionHandler`:
- `ResourceNotFoundException` => 404
- `WebExchangeBindException` => 400 erreur de validation
- `IllegalArgumentException` => 400 bad request

## Persistance et execution
- Base de donnees: H2 en memoire
- Mode schema JPA: `spring.jpa.hibernate.ddl-auto=update`
- Log SQL active
- HikariCP configure dans `application.properties`

## Guide de changement pour Copilot
Lors de la generation de code pour ce projet:
- conserver l'injection par constructeur
- ne pas melanger la logique repository dans les controleurs
- ne pas appeler directement des repositories JPA bloquants depuis les controleurs
- garder l'acces repository bloquant dans des wrappers reactifs de la couche service
- conserver les semantiques de reponse coherentes avec les controleurs actuels
- preferer un code CRUD simple et coherent plutot que d'introduire de nouvelles abstractions, sauf demande explicite de refactor
