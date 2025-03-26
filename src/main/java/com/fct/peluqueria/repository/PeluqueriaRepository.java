package com.fct.peluqueria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fct.peluqueria.models.Peluqueria;

public interface PeluqueriaRepository extends JpaRepository<Peluqueria, Integer> {

  @Query("SELECT p FROM Peluqueria p ORDER BY p.id ASC LIMIT 1")
  Peluqueria findFirst();
}
