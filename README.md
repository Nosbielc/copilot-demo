# copilot-demo

Spring Boot REST API com endpoints reativos, JPA, HikariCP e banco H2 em memória.

## Funcionalidades

- **CRUD de Alunos** — `GET/POST/PUT/DELETE /api/alunos`
- **CRUD de Salas** — `GET/POST/PUT/DELETE /api/salas`
- **CRUD de Professores** — `GET/POST/PUT/DELETE /api/professores`
- **CRUD de Agenda Escolar** — `GET/POST/PUT/DELETE /api/agenda`

## Tecnologias

| Tecnologia | Descrição |
|---|---|
| Spring Boot 3.2 | Framework principal |
| Spring WebFlux | Endpoints reativos (Mono/Flux) |
| Spring Data JPA | Acesso a dados com Hibernate |
| HikariCP | Pool de conexões |
| H2 Database | Banco de dados em memória |
| Jakarta Validation | Validação de entrada |

## Como Executar

```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.  
O console H2 estará disponível em `http://localhost:8080/h2-console`
(JDBC URL: `jdbc:h2:mem:escola`, usuário: `sa`, senha em branco).

## Endpoints

### Alunos

| Método | URL | Descrição |
|---|---|---|
| GET | `/api/alunos` | Listar todos os alunos |
| GET | `/api/alunos/{id}` | Buscar aluno por ID |
| POST | `/api/alunos` | Criar novo aluno |
| PUT | `/api/alunos/{id}` | Atualizar aluno |
| DELETE | `/api/alunos/{id}` | Deletar aluno |

**Exemplo de payload:**
```json
{
  "nome": "João Silva",
  "email": "joao@escola.com",
  "matricula": "MAT001"
}
```

### Salas

| Método | URL | Descrição |
|---|---|---|
| GET | `/api/salas` | Listar todas as salas |
| GET | `/api/salas/{id}` | Buscar sala por ID |
| POST | `/api/salas` | Criar nova sala |
| PUT | `/api/salas/{id}` | Atualizar sala |
| DELETE | `/api/salas/{id}` | Deletar sala |

**Exemplo de payload:**
```json
{
  "nome": "Sala de Informática",
  "numero": "101",
  "capacidade": 30
}
```

### Professores

| Método | URL | Descrição |
|---|---|---|
| GET | `/api/professores` | Listar todos os professores |
| GET | `/api/professores/{id}` | Buscar professor por ID |
| POST | `/api/professores` | Criar novo professor |
| PUT | `/api/professores/{id}` | Atualizar professor |
| DELETE | `/api/professores/{id}` | Deletar professor |

**Exemplo de payload:**
```json
{
  "nome": "Ana Costa",
  "email": "ana@escola.com",
  "disciplina": "Matemática"
}
```

### Agenda Escolar

| Método | URL | Descrição |
|---|---|---|
| GET | `/api/agenda` | Listar todas as agendas |
| GET | `/api/agenda/{id}` | Buscar agenda por ID |
| GET | `/api/agenda/data/{data}` | Buscar agendas por data (yyyy-MM-dd) |
| GET | `/api/agenda/aluno/{alunoId}` | Buscar agendas por aluno |
| GET | `/api/agenda/professor/{professorId}` | Buscar agendas por professor |
| GET | `/api/agenda/sala/{salaId}` | Buscar agendas por sala |
| POST | `/api/agenda` | Criar nova agenda |
| PUT | `/api/agenda/{id}` | Atualizar agenda |
| DELETE | `/api/agenda/{id}` | Deletar agenda |

**Exemplo de payload:**
```json
{
  "data": "2024-06-15",
  "hora": "08:00:00",
  "descricao": "Aula de Matemática",
  "aluno": { "id": 1 },
  "professor": { "id": 1 },
  "sala": { "id": 1 }
}
```

## Executar Testes

```bash
mvn test
```
