package com.example.demo;

import com.example.demo.entity.Aluno;
import com.example.demo.entity.Professor;
import com.example.demo.entity.Sala;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CopilotDemoApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @BeforeEach
    void setUp() {
        alunoRepository.deleteAll();
        salaRepository.deleteAll();
        professorRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    // --- Aluno CRUD Tests ---

    @Test
    void deveCriarAluno() {
        Aluno aluno = new Aluno("João Silva", "joao@escola.com", "MAT001");

        webTestClient.post().uri("/api/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(aluno)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Aluno.class)
                .value(a -> {
                    assertThat(a.getId()).isNotNull();
                    assertThat(a.getNome()).isEqualTo("João Silva");
                    assertThat(a.getEmail()).isEqualTo("joao@escola.com");
                    assertThat(a.getMatricula()).isEqualTo("MAT001");
                });
    }

    @Test
    void deveListarAlunos() {
        alunoRepository.save(new Aluno("Maria Santos", "maria@escola.com", "MAT002"));

        webTestClient.get().uri("/api/alunos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Aluno.class)
                .hasSize(1);
    }

    @Test
    void deveRetornar404ParaAlunoInexistente() {
        webTestClient.get().uri("/api/alunos/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deveDeletarAluno() {
        Aluno saved = alunoRepository.save(new Aluno("Carlos Lima", "carlos@escola.com", "MAT003"));

        webTestClient.delete().uri("/api/alunos/" + saved.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    // --- Sala CRUD Tests ---

    @Test
    void deveCriarSala() {
        Sala sala = new Sala("Sala de Informática", "101", 30);

        webTestClient.post().uri("/api/salas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sala)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Sala.class)
                .value(s -> {
                    assertThat(s.getId()).isNotNull();
                    assertThat(s.getNome()).isEqualTo("Sala de Informática");
                    assertThat(s.getNumero()).isEqualTo("101");
                    assertThat(s.getCapacidade()).isEqualTo(30);
                });
    }

    @Test
    void deveListarSalas() {
        salaRepository.save(new Sala("Sala A", "201", 25));

        webTestClient.get().uri("/api/salas")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Sala.class)
                .hasSize(1);
    }

    // --- Professor CRUD Tests ---

    @Test
    void deveCriarProfessor() {
        Professor professor = new Professor("Ana Costa", "ana@escola.com", "Matemática");

        webTestClient.post().uri("/api/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(professor)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Professor.class)
                .value(p -> {
                    assertThat(p.getId()).isNotNull();
                    assertThat(p.getNome()).isEqualTo("Ana Costa");
                    assertThat(p.getDisciplina()).isEqualTo("Matemática");
                });
    }

    @Test
    void deveListarProfessores() {
        professorRepository.save(new Professor("Pedro Oliveira", "pedro@escola.com", "Física"));

        webTestClient.get().uri("/api/professores")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Professor.class)
                .hasSize(1);
    }
}
