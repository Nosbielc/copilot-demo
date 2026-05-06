package com.example.demo.service;

import com.example.demo.entity.Professor;
import com.example.demo.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public Flux<Professor> listarTodos() {
        return Mono.fromCallable(professorRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Professor> buscarPorId(Long id) {
        return Mono.fromCallable(() -> professorRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> opt.map(Mono::just).orElseGet(Mono::empty));
    }

    @Transactional
    public Mono<Professor> salvar(Professor professor) {
        return Mono.fromCallable(() -> professorRepository.save(professor))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Professor> atualizar(Long id, Professor professorAtualizado) {
        return Mono.fromCallable(() -> professorRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> opt.map(professor -> {
                    professor.setNome(professorAtualizado.getNome());
                    professor.setEmail(professorAtualizado.getEmail());
                    professor.setDisciplina(professorAtualizado.getDisciplina());
                    return Mono.fromCallable(() -> professorRepository.save(professor))
                            .subscribeOn(Schedulers.boundedElastic());
                }).orElseGet(Mono::empty));
    }

    @Transactional
    public Mono<Void> deletar(Long id) {
        return Mono.fromRunnable(() -> professorRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
