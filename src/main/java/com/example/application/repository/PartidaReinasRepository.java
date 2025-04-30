package com.example.application.repository;

import com.example.application.model.PartidaReinas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaReinasRepository extends JpaRepository<PartidaReinas, Long> {
}
