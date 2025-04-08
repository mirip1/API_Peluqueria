package com.fct.peluqueria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fct.peluqueria.constants.DiaSemana;
import com.fct.peluqueria.models.HorarioBase;

/**
 * Clase que se encarga de las operaciones a la BBDD de la tabla horario
 */
public interface HorarioBaseRepository extends JpaRepository<HorarioBase, Integer> {

  @Query("SELECT hb FROM HorarioBase hb WHERE hb.diaSemana = :diaSemana")
  List<HorarioBase> findByDiaSemana(@Param("diaSemana") DiaSemana diaSemana);
}
