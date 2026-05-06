package com.example.demo.controller;

import com.example.demo.entity.Professor;
import com.example.demo.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public Flux<Professor> listarTodos() {
        return professorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Mono<Professor> buscarPorId(@PathVariable Long id) {
        return professorService.buscarPorId(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Professor não encontrado com id: " + id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Professor> criar(@Valid @RequestBody Professor professor) {
        return professorService.salvar(professor);
    }

    @PutMapping("/{id}")
    public Mono<Professor> atualizar(@PathVariable Long id, @Valid @RequestBody Professor professor) {
        return professorService.atualizar(id, professor)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Professor não encontrado com id: " + id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletar(@PathVariable Long id) {
        return professorService.deletar(id);
    }
}
