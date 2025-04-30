package com.example.application.repository;

import com.example.application.model.PartidaHanoi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaHanoiRepository extends JpaRepository<PartidaHanoi, Long> {
}