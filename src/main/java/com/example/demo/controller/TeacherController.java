package com.example.demo.controller;

import com.example.demo.entity.Teacher;
import com.example.demo.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public Flux<Teacher> listAll() {
        return teacherService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Teacher> findById(@PathVariable Long id) {
        return teacherService.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Teacher not found with id: " + id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Teacher> create(@Valid @RequestBody Teacher teacher) {
        return teacherService.save(teacher);
    }

    @PutMapping("/{id}")
    public Mono<Teacher> update(@PathVariable Long id, @Valid @RequestBody Teacher teacher) {
        return teacherService.update(id, teacher)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Teacher not found with id: " + id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return teacherService.delete(id);
    }
}
