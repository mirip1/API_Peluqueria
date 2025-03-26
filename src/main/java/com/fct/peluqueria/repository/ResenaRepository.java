package com.fct.peluqueria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fct.peluqueria.models.Resena;
import com.fct.peluqueria.models.Usuario;

public interface ResenaRepository extends JpaRepository<Resena, Integer> {

  @Query("SELECT r FROM Resena r WHERE r.usuario = :usuario ORDER BY r.fecha DESC")
  List<Resena> findByUsuario(@Param("usuario") Usuario usuario);
}