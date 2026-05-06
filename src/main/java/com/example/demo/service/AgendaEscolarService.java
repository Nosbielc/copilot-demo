package com.example.demo.service;

import com.example.demo.entity.AgendaEscolar;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.Professor;
import com.example.demo.entity.Sala;
import com.example.demo.repository.AgendaEscolarRepository;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.SalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@Service
public class AgendaEscolarService {

    private final AgendaEscolarRepository agendaEscolarRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final SalaRepository salaRepository;

    public AgendaEscolarService(AgendaEscolarRepository agendaEscolarRepository,
                                AlunoRepository alunoRepository,
                                ProfessorRepository professorRepository,
                                SalaRepository salaRepository) {
        this.agendaEscolarRepository = agendaEscolarRepository;
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.salaRepository = salaRepository;
    }

    public Flux<AgendaEscolar> listarTodas() {
        return Mono.fromCallable(agendaEscolarRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<AgendaEscolar> buscarPorId(Long id) {
        return Mono.fromCallable(() -> agendaEscolarRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> opt.map(Mono::just).orElseGet(Mono::empty));
    }

    public Flux<AgendaEscolar> buscarPorData(LocalDate data) {
        return Mono.fromCallable(() -> agendaEscolarRepository.findByData(data))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<AgendaEscolar> buscarPorAluno(Long alunoId) {
        return Mono.fromCallable(() -> agendaEscolarRepository.findByAlunoId(alunoId))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<AgendaEscolar> buscarPorProfessor(Long professorId) {
        return Mono.fromCallable(() -> agendaEscolarRepository.findByProfessorId(professorId))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<AgendaEscolar> buscarPorSala(Long salaId) {
        return Mono.fromCallable(() -> agendaEscolarRepository.findBySalaId(salaId))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<AgendaEscolar> salvar(AgendaEscolar agenda) {
        return Mono.fromCallable(() -> {
            Aluno aluno = alunoRepository.findById(agenda.getAluno().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));
            Professor professor = professorRepository.findById(agenda.getProfessor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado"));
            Sala sala = salaRepository.findById(agenda.getSala().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada"));
            agenda.setAluno(aluno);
            agenda.setProfessor(professor);
            agenda.setSala(sala);
            return agendaEscolarRepository.save(agenda);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<AgendaEscolar> atualizar(Long id, AgendaEscolar agendaAtualizada) {
        return Mono.fromCallable(() -> {
            AgendaEscolar agenda = agendaEscolarRepository.findById(id)
                    .orElse(null);
            if (agenda == null) {
                return null;
            }
            Aluno aluno = alunoRepository.findById(agendaAtualizada.getAluno().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));
            Professor professor = professorRepository.findById(agendaAtualizada.getProfessor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado"));
            Sala sala = salaRepository.findById(agendaAtualizada.getSala().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada"));
            agenda.setData(agendaAtualizada.getData());
            agenda.setHora(agendaAtualizada.getHora());
            agenda.setDescricao(agendaAtualizada.getDescricao());
            agenda.setAluno(aluno);
            agenda.setProfessor(professor);
            agenda.setSala(sala);
            return agendaEscolarRepository.save(agenda);
        }).subscribeOn(Schedulers.boundedElastic())
                .flatMap(saved -> saved != null ? Mono.just(saved) : Mono.empty());
    }

    @Transactional
    public Mono<Void> deletar(Long id) {
        return Mono.fromRunnable(() -> agendaEscolarRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
