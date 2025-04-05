package com.fct.peluqueria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fct.peluqueria.models.Horario;

/**
 * Clase que se encarga de las operaciones a la BBDD de la tabla horario
 */
public interface HorarioRepository extends JpaRepository<Horario, Integer> {

  @Query("SELECT h FROM Horario h WHERE h.diaSemana = :diaSemana ORDER BY h.horaInicio")
  List<Horario> findByDiaSemana(@Param("diaSemana") String diaSemana);
}
