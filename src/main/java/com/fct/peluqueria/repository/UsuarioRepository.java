package com.fct.peluqueria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fct.peluqueria.constants.Rol;
import com.fct.peluqueria.models.Usuario;

/**
 * Clase que se encarga de las operaciones a la BBDD de la tabla usuarios
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  @Query("SELECT u FROM Usuario u WHERE u.email = :email")
  Optional<Usuario> findByEmail(@Param("email") String email);

  @Query("SELECT u FROM Usuario u WHERE u.baneado = true")
  List<Usuario> findUsuariosBaneados();
  
  List<Usuario> findByRol(Rol rol);
  
  boolean existsByEmail(String email);
}
