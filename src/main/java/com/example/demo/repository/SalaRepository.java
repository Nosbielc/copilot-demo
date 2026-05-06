package com.example.demo.repository;

import com.example.demo.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    Optional<Sala> findByNumero(String numero);
    boolean existsByNumero(String numero);
}
