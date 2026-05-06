# Contexte de test

## Setup de test actuel
Le projet contient deja des tests d'integration dans:
- `src/test/java/com/example/demo/CopilotDemoApplicationTests.java`

Ces tests utilisent:
- `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)`
- `@AutoConfigureWebTestClient`
- `WebTestClient`
- de vrais repositories sur la base H2 en memoire

Cela signifie que la suite actuelle est **orientee integration**, pas test unitaire pur.

## Bibliotheques de test disponibles
D'apres `pom.xml`, les dependances de test incluent:
- `spring-boot-starter-test`
- `reactor-test`

Dans les projets Spring Boot, `spring-boot-starter-test` fournit JUnit 5, AssertJ, Mockito et le support de test Spring.

## Strategie de test recommandee
### 1. Tests unitaires de service
Utiliser des tests unitaires pour isoler la logique service.

Outils recommandes:
- JUnit 5
- Mockito
- Reactor `StepVerifier`

Pattern:
- mocker les repositories avec `@Mock`
- creer le service avec `@InjectMocks`
- stubber les reponses repository avec `when(...)`
- verifier les resultats reactifs avec `StepVerifier`
- verifier les interactions avec `verify(...)`

Cibles prioritaires:
- `StudentService`
- `TeacherService`
- `RoomService`
- `SchoolScheduleService`

### 2. Tests d'integration de controleur
Utiliser des tests d'integration pour valider:
- le routing
- la serialisation requete/reponse
- le comportement de validation
- les status HTTP
- la gestion d'erreurs

Outil recommande:
- `WebTestClient`

### 3. Tests repository
Si la logique repository evolue, envisager des tests JPA focalises pour les requetes personnalisees.
Pour l'instant, les repositories sont simples; la couverture service + integration est souvent suffisante.

## Note importante sur les tests reactifs
Les methodes de service retournent `Mono` et `Flux`, mais reposent en interne sur des appels JPA bloquants deplaces sur `Schedulers.boundedElastic()`.
Lors des tests unitaires de service:
- souscrire aux publishers au lieu d'appeler directement les repositories
- preferer `StepVerifier` a `.block()` dans les assertions
- verifier les publishers vides pour les cas not-found quand applicable

## Guide specifique par service
### `StudentService`, `TeacherService`, `RoomService`
Cas typiques a tester:
- find all
- find by id quand trouve
- find by id quand absent
- save
- update quand trouve
- update quand absent
- delete

### `SchoolScheduleService`
Cas supplementaires a tester:
- save resout les relations `Student`, `Teacher` et `Room`
- save echoue avec `IllegalArgumentException` si une entite liee est absente
- update retourne vide quand l'id du planning n'existe pas
- update echoue si un id d'entite liee est invalide
- les filtres date/student/teacher/room deleguent bien au repository

## Exemple de structure de test unitaire
```java
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void shouldFindStudentById() {
        Student student = new Student("John Smith", "john@school.com", "MAT001");
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StepVerifier.create(studentService.findById(1L))
                .expectNextMatches(found -> found.getId().equals(1L))
                .verifyComplete();

        verify(studentRepository).findById(1L);
    }
}
```

## Guide Copilot
Lors de la generation de nouveaux tests pour ce repo:
- distinguer clairement tests unitaires et tests d'integration
- utiliser Mockito pour isoler les repositories dans les tests de service
- utiliser `WebTestClient` pour la verification endpoint
- conserver des noms de test orientes comportement, ex: `shouldCreateStudent`, `shouldReturn404ForNonExistentStudent`
