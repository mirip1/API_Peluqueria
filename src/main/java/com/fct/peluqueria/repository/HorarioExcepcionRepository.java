package com.fct.peluqueria.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fct.peluqueria.models.HorarioExcepcion;

/**
 * Clase que se encarga de las operaciones a la BBDD de la tabla horario
 */
public interface HorarioExcepcionRepository extends JpaRepository<HorarioExcepcion, Integer> {

  List<HorarioExcepcion> findByFecha(LocalDate fecha);
}
