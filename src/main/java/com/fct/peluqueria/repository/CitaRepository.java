package com.fct.peluqueria.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fct.peluqueria.constants.EstadoCita;
import com.fct.peluqueria.models.Cita;
import com.fct.peluqueria.models.Usuario;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

  @Query("SELECT c FROM Cita c WHERE c.usuario = :usuario AND c.estado = :estado")
  List<Cita> findByUsuarioAndEstado(@Param("usuario") Usuario usuario, @Param("estado") EstadoCita estado);

  @Query("SELECT c FROM Cita c WHERE c.fechaYHora BETWEEN :inicio AND :fin")
  List<Cita> findByFechaYHoraBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}