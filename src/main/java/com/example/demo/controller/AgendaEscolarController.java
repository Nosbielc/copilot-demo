package com.example.demo.controller;

import com.example.demo.entity.AgendaEscolar;
import com.example.demo.service.AgendaEscolarService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/agenda")
public class AgendaEscolarController {

    private final AgendaEscolarService agendaEscolarService;

    public AgendaEscolarController(AgendaEscolarService agendaEscolarService) {
        this.agendaEscolarService = agendaEscolarService;
    }

    @GetMapping
    public Flux<AgendaEscolar> listarTodas() {
        return agendaEscolarService.listarTodas();
    }

    @GetMapping("/{id}")
    public Mono<AgendaEscolar> buscarPorId(@PathVariable Long id) {
        return agendaEscolarService.buscarPorId(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Agenda não encontrada com id: " + id)));
    }

    @GetMapping("/data/{data}")
    public Flux<AgendaEscolar> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return agendaEscolarService.buscarPorData(data);
    }

    @GetMapping("/aluno/{alunoId}")
    public Flux<AgendaEscolar> buscarPorAluno(@PathVariable Long alunoId) {
        return agendaEscolarService.buscarPorAluno(alunoId);
    }

    @GetMapping("/professor/{professorId}")
    public Flux<AgendaEscolar> buscarPorProfessor(@PathVariable Long professorId) {
        return agendaEscolarService.buscarPorProfessor(professorId);
    }

    @GetMapping("/sala/{salaId}")
    public Flux<AgendaEscolar> buscarPorSala(@PathVariable Long salaId) {
        return agendaEscolarService.buscarPorSala(salaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AgendaEscolar> criar(@Valid @RequestBody AgendaEscolar agenda) {
        return agendaEscolarService.salvar(agenda);
    }

    @PutMapping("/{id}")
    public Mono<AgendaEscolar> atualizar(@PathVariable Long id, @Valid @RequestBody AgendaEscolar agenda) {
        return agendaEscolarService.atualizar(id, agenda)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Agenda não encontrada com id: " + id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletar(@PathVariable Long id) {
        return agendaEscolarService.deletar(id);
    }
}
