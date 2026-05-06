package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Flux<Student> findAll() {
        return Mono.fromCallable(studentRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Student> findById(Long id) {
        return Mono.fromCallable(() -> studentRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> opt.map(Mono::just).orElseGet(Mono::empty));
    }

    @Transactional
    public Mono<Student> save(Student student) {
        return Mono.fromCallable(() -> studentRepository.save(student))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Student> update(Long id, Student updatedStudent) {
        return Mono.fromCallable(() -> studentRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> {
                    if (opt.isEmpty()) {
                        return Mono.<Student>empty();
                    }
                    Student student = opt.get();
                    student.setName(updatedStudent.getName());
                    student.setEmail(updatedStudent.getEmail());
                    student.setEnrollmentNumber(updatedStudent.getEnrollmentNumber());
                    return Mono.fromCallable(() -> studentRepository.save(student))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }

    @Transactional
    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> studentRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
