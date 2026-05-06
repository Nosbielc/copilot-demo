package com.example.demo.controller;

import com.example.demo.entity.Aluno;
import com.example.demo.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public Flux<Aluno> listarTodos() {
        return alunoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Mono<Aluno> buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Aluno não encontrado com id: " + id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Aluno> criar(@Valid @RequestBody Aluno aluno) {
        return alunoService.salvar(aluno);
    }

    @PutMapping("/{id}")
    public Mono<Aluno> atualizar(@PathVariable Long id, @Valid @RequestBody Aluno aluno) {
        return alunoService.atualizar(id, aluno)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Aluno não encontrado com id: " + id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletar(@PathVariable Long id) {
        return alunoService.deletar(id);
    }
}
