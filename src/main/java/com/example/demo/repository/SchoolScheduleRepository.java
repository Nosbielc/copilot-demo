package com.example.demo.repository;

import com.example.demo.entity.SchoolSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SchoolScheduleRepository extends JpaRepository<SchoolSchedule, Long> {
    List<SchoolSchedule> findByDate(LocalDate date);
    List<SchoolSchedule> findByStudentId(Long studentId);
    List<SchoolSchedule> findByTeacherId(Long teacherId);
    List<SchoolSchedule> findByRoomId(Long roomId);
}
