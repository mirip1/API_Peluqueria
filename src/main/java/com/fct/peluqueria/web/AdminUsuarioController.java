package com.fct.peluqueria.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fct.peluqueria.dto.UsuarioDTO;
import com.fct.peluqueria.service.UsuarioService;

/**
 * Controlador de Superusuarios
 */
@RestController
@RequestMapping("/api/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  /**
   * Método para listar todos los usuarios existentes
   * 
   * @return Lista de usuarios
   */
  @GetMapping
  public List<UsuarioDTO> listAll() {
    return usuarioService.listAllUsuarios();
  }

  /**
   * Método para banear un usuario
   * 
   * @param id del usuario a banear
   * @return respuesta de la llamada
   * @throws NotFoundException 
   */
  @PutMapping("/{id}/ban")
  public ResponseEntity<?> banUser(@PathVariable Integer id) throws NotFoundException {
    usuarioService.banearUsuarioById(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Método para desbanear un usuario
   * 
   * @param id del usuario a banear
   * @return respuesta de la llamada
   * @throws NotFoundException 
   */
  @PutMapping("/{id}/unban")
  public ResponseEntity<?> unbanUser(@PathVariable Integer id) throws NotFoundException {
    usuarioService.desbanearUsuarioById(id);
    return ResponseEntity.noContent().build();
  }
}