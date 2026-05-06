package com.example.demo;

import com.example.demo.entity.Room;
import com.example.demo.entity.Student;
import com.example.demo.entity.Teacher;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TeacherRepository;
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
    private StudentRepository studentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        roomRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    // --- Student CRUD Tests ---

    @Test
    void shouldCreateStudent() {
        Student student = new Student("John Smith", "john@school.com", "MAT001");

        webTestClient.post().uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(student)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Student.class)
                .value(a -> {
                    assertThat(a.getId()).isNotNull();
                    assertThat(a.getName()).isEqualTo("John Smith");
                    assertThat(a.getEmail()).isEqualTo("john@school.com");
                    assertThat(a.getEnrollmentNumber()).isEqualTo("MAT001");
                });
    }

    @Test
    void shouldListStudents() {
        studentRepository.save(new Student("Mary Johnson", "mary@school.com", "MAT002"));

        webTestClient.get().uri("/api/students")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Student.class)
                .hasSize(1);
    }

    @Test
    void shouldReturn404ForNonExistentStudent() {
        webTestClient.get().uri("/api/students/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldDeleteStudent() {
        Student saved = studentRepository.save(new Student("Charles Lima", "charles@school.com", "MAT003"));

        webTestClient.delete().uri("/api/students/" + saved.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    // --- Room CRUD Tests ---

    @Test
    void shouldCreateRoom() {
        Room room = new Room("Computer Lab", "101", 30);

        webTestClient.post().uri("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(room)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Room.class)
                .value(s -> {
                    assertThat(s.getId()).isNotNull();
                    assertThat(s.getName()).isEqualTo("Computer Lab");
                    assertThat(s.getNumber()).isEqualTo("101");
                    assertThat(s.getCapacity()).isEqualTo(30);
                });
    }

    @Test
    void shouldListRooms() {
        roomRepository.save(new Room("Room A", "201", 25));

        webTestClient.get().uri("/api/rooms")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Room.class)
                .hasSize(1);
    }

    // --- Teacher CRUD Tests ---

    @Test
    void shouldCreateTeacher() {
        Teacher teacher = new Teacher("Anne Costa", "anne@school.com", "Mathematics");

        webTestClient.post().uri("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(teacher)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Teacher.class)
                .value(p -> {
                    assertThat(p.getId()).isNotNull();
                    assertThat(p.getName()).isEqualTo("Anne Costa");
                    assertThat(p.getSubject()).isEqualTo("Mathematics");
                });
    }

    @Test
    void shouldListTeachers() {
        teacherRepository.save(new Teacher("Peter Oliveira", "peter@school.com", "Physics"));

        webTestClient.get().uri("/api/teachers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Teacher.class)
                .hasSize(1);
    }
}
