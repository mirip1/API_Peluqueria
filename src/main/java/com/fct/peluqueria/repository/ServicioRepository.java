package com.fct.peluqueria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fct.peluqueria.models.Servicio;

/**
 * Clase que se encarga de las operaciones a la BBDD de la tabla servicios
 */
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

  @Query("SELECT s FROM Servicio s WHERE LOWER(s.nombre) = LOWER(:nombre)")
  Servicio findByNombre(@Param("nombre") String nombre);
}