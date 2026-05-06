package com.example.demo.controller;

import com.example.demo.entity.SchoolSchedule;
import com.example.demo.service.SchoolScheduleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/schedule")
public class SchoolScheduleController {

    private final SchoolScheduleService schoolScheduleService;

    public SchoolScheduleController(SchoolScheduleService schoolScheduleService) {
        this.schoolScheduleService = schoolScheduleService;
    }

    @GetMapping
    public Flux<SchoolSchedule> listAll() {
        return schoolScheduleService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<SchoolSchedule> findById(@PathVariable Long id) {
        return schoolScheduleService.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Schedule not found with id: " + id)));
    }

    @GetMapping("/date/{date}")
    public Flux<SchoolSchedule> findByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return schoolScheduleService.findByDate(date);
    }

    @GetMapping("/student/{studentId}")
    public Flux<SchoolSchedule> findByStudent(@PathVariable Long studentId) {
        return schoolScheduleService.findByStudent(studentId);
    }

    @GetMapping("/teacher/{teacherId}")
    public Flux<SchoolSchedule> findByTeacher(@PathVariable Long teacherId) {
        return schoolScheduleService.findByTeacher(teacherId);
    }

    @GetMapping("/room/{roomId}")
    public Flux<SchoolSchedule> findByRoom(@PathVariable Long roomId) {
        return schoolScheduleService.findByRoom(roomId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SchoolSchedule> create(@Valid @RequestBody SchoolSchedule schedule) {
        return schoolScheduleService.save(schedule);
    }

    @PutMapping("/{id}")
    public Mono<SchoolSchedule> update(@PathVariable Long id, @Valid @RequestBody SchoolSchedule schedule) {
        return schoolScheduleService.update(id, schedule)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Schedule not found with id: " + id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return schoolScheduleService.delete(id);
    }
}
