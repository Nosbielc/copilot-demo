package com.example.demo.service;

import com.example.demo.entity.Aluno;
import com.example.demo.repository.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Flux<Aluno> listarTodos() {
        return Mono.fromCallable(alunoRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Aluno> buscarPorId(Long id) {
        return Mono.fromCallable(() -> alunoRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> opt.map(Mono::just).orElseGet(Mono::empty));
    }

    @Transactional
    public Mono<Aluno> salvar(Aluno aluno) {
        return Mono.fromCallable(() -> alunoRepository.save(aluno))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Aluno> atualizar(Long id, Aluno alunoAtualizado) {
        return Mono.fromCallable(() -> alunoRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> opt.map(aluno -> {
                    aluno.setNome(alunoAtualizado.getNome());
                    aluno.setEmail(alunoAtualizado.getEmail());
                    aluno.setMatricula(alunoAtualizado.getMatricula());
                    return Mono.fromCallable(() -> alunoRepository.save(aluno))
                            .subscribeOn(Schedulers.boundedElastic());
                }).orElseGet(Mono::empty));
    }

    @Transactional
    public Mono<Void> deletar(Long id) {
        return Mono.fromRunnable(() -> alunoRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
