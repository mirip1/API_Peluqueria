package com.fct.peluqueria.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fct.peluqueria.constants.EstadoCita;
import com.fct.peluqueria.models.Cita;

/**
 * Clase que se encarga de las operaciones a la BBDD de la tabla citas
 */
public interface CitaRepository extends JpaRepository<Cita, Integer> {
  List<Cita> findAllByEstado(EstadoCita estado);

  List<Cita> findByUsuarioId(Integer usuarioId);

  Optional<Cita> findByUsuarioIdAndFechaYHoraAndEstado(Integer usuarioId, LocalDateTime fechaYHora, EstadoCita estado);

  List<Cita> findAllByEstadoAndFechaYHoraBetween(EstadoCita estado, LocalDateTime inicio, LocalDateTime fin);

}