package com.example.demo.service;

import com.example.demo.entity.Teacher;
import com.example.demo.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Flux<Teacher> findAll() {
        return Mono.fromCallable(teacherRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Teacher> findById(Long id) {
        return Mono.fromCallable(() -> teacherRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> opt.map(Mono::just).orElseGet(Mono::empty));
    }

    @Transactional
    public Mono<Teacher> save(Teacher teacher) {
        return Mono.fromCallable(() -> teacherRepository.save(teacher))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Teacher> update(Long id, Teacher updatedTeacher) {
        return Mono.fromCallable(() -> teacherRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> {
                    if (opt.isEmpty()) {
                        return Mono.<Teacher>empty();
                    }
                    Teacher teacher = opt.get();
                    teacher.setName(updatedTeacher.getName());
                    teacher.setEmail(updatedTeacher.getEmail());
                    teacher.setSubject(updatedTeacher.getSubject());
                    return Mono.fromCallable(() -> teacherRepository.save(teacher))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }

    @Transactional
    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> teacherRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
