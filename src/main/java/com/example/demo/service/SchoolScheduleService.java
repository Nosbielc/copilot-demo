package com.example.demo.service;

import com.example.demo.entity.Room;
import com.example.demo.entity.SchoolSchedule;
import com.example.demo.entity.Student;
import com.example.demo.entity.Teacher;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.SchoolScheduleRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@Service
public class SchoolScheduleService {

    private final SchoolScheduleRepository schoolScheduleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final RoomRepository roomRepository;

    public SchoolScheduleService(SchoolScheduleRepository schoolScheduleRepository,
                                 StudentRepository studentRepository,
                                 TeacherRepository teacherRepository,
                                 RoomRepository roomRepository) {
        this.schoolScheduleRepository = schoolScheduleRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.roomRepository = roomRepository;
    }

    public Flux<SchoolSchedule> findAll() {
        return Mono.fromCallable(schoolScheduleRepository::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<SchoolSchedule> findById(Long id) {
        return Mono.fromCallable(() -> schoolScheduleRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(opt -> opt.map(Mono::just).orElseGet(Mono::empty));
    }

    public Flux<SchoolSchedule> findByDate(LocalDate date) {
        return Mono.fromCallable(() -> schoolScheduleRepository.findByDate(date))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<SchoolSchedule> findByStudent(Long studentId) {
        return Mono.fromCallable(() -> schoolScheduleRepository.findByStudentId(studentId))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<SchoolSchedule> findByTeacher(Long teacherId) {
        return Mono.fromCallable(() -> schoolScheduleRepository.findByTeacherId(teacherId))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<SchoolSchedule> findByRoom(Long roomId) {
        return Mono.fromCallable(() -> schoolScheduleRepository.findByRoomId(roomId))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<SchoolSchedule> save(SchoolSchedule schedule) {
        return Mono.fromCallable(() -> {
            Student student = studentRepository.findById(schedule.getStudent().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + schedule.getStudent().getId()));
            Teacher teacher = teacherRepository.findById(schedule.getTeacher().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + schedule.getTeacher().getId()));
            Room room = roomRepository.findById(schedule.getRoom().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + schedule.getRoom().getId()));
            schedule.setStudent(student);
            schedule.setTeacher(teacher);
            schedule.setRoom(room);
            return schoolScheduleRepository.save(schedule);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<SchoolSchedule> update(Long id, SchoolSchedule updatedSchedule) {
        return Mono.fromCallable(() -> {
            SchoolSchedule schedule = schoolScheduleRepository.findById(id)
                    .orElse(null);
            if (schedule == null) {
                return null;
            }
            Student student = studentRepository.findById(updatedSchedule.getStudent().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + updatedSchedule.getStudent().getId()));
            Teacher teacher = teacherRepository.findById(updatedSchedule.getTeacher().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + updatedSchedule.getTeacher().getId()));
            Room room = roomRepository.findById(updatedSchedule.getRoom().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + updatedSchedule.getRoom().getId()));
            schedule.setDate(updatedSchedule.getDate());
            schedule.setTime(updatedSchedule.getTime());
            schedule.setDescription(updatedSchedule.getDescription());
            schedule.setStudent(student);
            schedule.setTeacher(teacher);
            schedule.setRoom(room);
            return schoolScheduleRepository.save(schedule);
        }).subscribeOn(Schedulers.boundedElastic())
                .flatMap(saved -> saved != null ? Mono.just(saved) : Mono.empty());
    }

    @Transactional
    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> schoolScheduleRepository.deleteById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
