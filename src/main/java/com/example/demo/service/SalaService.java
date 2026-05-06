package com.example.demo.service;

import com.example.demo.entity.Sala;
import com.example.demo.repository.SalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class SalaService {

    private final SalaRepository salaRepository;

    public SalaService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public Flux<Sala> listarTodas() {
        return Mono.fromCallable(salaRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Sala> buscarPorId(Long id) {
        return Mono.fromCallable(() -> salaRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> opt.map(Mono::just).orElseGet(Mono::empty));
    }

    @Transactional
    public Mono<Sala> salvar(Sala sala) {
        return Mono.fromCallable(() -> salaRepository.save(sala))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Sala> atualizar(Long id, Sala salaAtualizada) {
        return Mono.fromCallable(() -> salaRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> {
                    if (opt.isEmpty()) {
                        return Mono.<Sala>empty();
                    }
                    Sala sala = opt.get();
                    sala.setNome(salaAtualizada.getNome());
                    sala.setNumero(salaAtualizada.getNumero());
                    sala.setCapacidade(salaAtualizada.getCapacidade());
                    return Mono.fromCallable(() -> salaRepository.save(sala))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }

    @Transactional
    public Mono<Void> deletar(Long id) {
        return Mono.fromRunnable(() -> salaRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
