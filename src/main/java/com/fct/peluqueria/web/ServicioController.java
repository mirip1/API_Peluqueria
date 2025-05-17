package com.fct.peluqueria.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fct.peluqueria.dto.ServicioDTO;
import com.fct.peluqueria.service.ServicioService;

/**
 * Controller de Servicio
 */
@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

  @Autowired
  private ServicioService servicioService;

  /**
   * Metodo para coger la lista de Servicios
   * 
   * @return respuesta de la llamada
   */
  @GetMapping
  public ResponseEntity<?> getServicios() {
    return ResponseEntity.ok(servicioService.getAllServicios());
  }

  /**
   * Metodo para crear un Servicio
   * 
   * @param servicio a añadir
   * @return el nuevo servicio
   */
  @PostMapping
  public ResponseEntity<ServicioDTO> crearServicio(@RequestBody ServicioDTO servicio) {
    ServicioDTO creado = servicioService.crearServicio(servicio);
    return ResponseEntity.ok(creado);
  }

  /**
   * Metodo para crear un Servicio
   * 
   * @param id       del Servicio a actualizar
   * @param servicio a actualizar
   * @return servicio actualizado
   */
  @PutMapping("/{id}")
  public ResponseEntity<ServicioDTO> actualizarServicio(@PathVariable Integer id, @RequestBody ServicioDTO servicio) {
    ServicioDTO actualizado = servicioService.updateServicio(id, servicio);
    return ResponseEntity.ok(actualizado);
  }

  /**
   * Metedo para borrar un servicio
   * 
   * @param id del servicio a borrar
   * @return codigo 200 si es funciona
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> borrarServicio(@PathVariable Integer id) {
    servicioService.deleteServicio(id);
    return ResponseEntity.noContent().build();
  }
}
