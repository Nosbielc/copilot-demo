package com.example.demo.repository;

import com.example.demo.entity.AgendaEscolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendaEscolarRepository extends JpaRepository<AgendaEscolar, Long> {
    List<AgendaEscolar> findByData(LocalDate data);
    List<AgendaEscolar> findByAlunoId(Long alunoId);
    List<AgendaEscolar> findByProfessorId(Long professorId);
    List<AgendaEscolar> findBySalaId(Long salaId);
}
