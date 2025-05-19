package com.fct.peluqueria.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fct.peluqueria.dto.CitaDTO;
import com.fct.peluqueria.service.CitaService;

/**
 * Controlador de Citas
 */
@RestController
@RequestMapping("/api/citas")
public class CitaController {

  @Autowired
  private CitaService citaService;

  /**
   * Metodo para crear una cita
   * 
   * @param citaDTO JSON de la Cita a crear
   * @return respuesta de la llamada
   */
  @PostMapping
  public ResponseEntity<?> crearCita(@RequestBody CitaDTO citaDTO) {
    return ResponseEntity.ok(citaService.crearCita(citaDTO));
  }

  /**
   * Metodo para coger las citas en base al id del usuario devuelve una lista de
   * citas
   * 
   * @param usuarioId Identificador del usuario
   * @return respuesta de la llamada
   */
  @GetMapping("/usuario/{usuarioId}")
  public ResponseEntity<?> getCitasPorUsuario(@PathVariable Integer usuarioId) {
    return ResponseEntity.ok(citaService.obtenerCitasPorUsuario(usuarioId));
  }

  /**
   * Metodo que actualiza el estado de una cita(Admin)
   * 
   * @param id identificador de la cita
   * @return respuesta de la llamada
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<?> cancelarCita(@PathVariable Integer id) {
    citaService.cancelarCita(id);
    return ResponseEntity.noContent().build();
  }
  
  /**
   * Borra totalmente una cita de la BBDD.
   * Solo un usuario puede borrar SU propia cita activa.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCita(@PathVariable Integer id, Principal principal) {
    citaService.eliminarCita(id, principal.getName());
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<CitaDTO>> list() {
    return ResponseEntity.ok(citaService.getAllCitas());
  }
}