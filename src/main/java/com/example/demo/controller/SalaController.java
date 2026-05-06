package com.example.demo.controller;

import com.example.demo.entity.Sala;
import com.example.demo.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/salas")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping
    public Flux<Sala> listarTodas() {
        return salaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Mono<Sala> buscarPorId(@PathVariable Long id) {
        return salaService.buscarPorId(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sala não encontrada com id: " + id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Sala> criar(@Valid @RequestBody Sala sala) {
        return salaService.salvar(sala);
    }

    @PutMapping("/{id}")
    public Mono<Sala> atualizar(@PathVariable Long id, @Valid @RequestBody Sala sala) {
        return salaService.atualizar(id, sala)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sala não encontrada com id: " + id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletar(@PathVariable Long id) {
        return salaService.deletar(id);
    }
}
