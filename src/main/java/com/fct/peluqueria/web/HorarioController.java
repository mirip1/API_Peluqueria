package com.fct.peluqueria.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fct.peluqueria.dto.DisponibilidadDiaDTO;
import com.fct.peluqueria.dto.HorarioDTO;
import com.fct.peluqueria.service.HorarioService;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    /**
     * @param fechaStr la fecha en formato yyyy-MM-dd.
     * @return Lista de HorarioDTO resultante (combinación de horario base y excepciones).
     */
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<?> getHorariosPorFecha(@PathVariable("fecha") String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr); 
        return ResponseEntity.ok(horarioService.obtenerHorariosPorFecha(fecha));
    }
    /**
     * @param id         identificador del horario base.
     * @param horarioDTO JSON con los nuevos datos del horario.
     * @return HorarioDTO actualizado.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHorario(@PathVariable Integer id, @RequestBody HorarioDTO horarioDTO) {
        return ResponseEntity.ok(horarioService.updateHorarioBase(id, horarioDTO));
    }

    /**
     * @param excepcionDTO JSON con la información de la excepción.
     * @param fechaStr     La fecha en la que se aplica la excepción.
     * @return HorarioDTO de la excepción creada.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/excepcion")
    public ResponseEntity<?> addExcepcionHorario(@RequestBody HorarioDTO excepcionDTO, @RequestParam("fecha") String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        return ResponseEntity.ok(horarioService.addExcepcionHorario(excepcionDTO, fecha));
    }
    
    /**
     * Devuelve la la lista disponibilidad de cada deia de un mes en especifico
     * 
     * @param year  año
     * @param month mes
     * @return lista de disponibilidad por dia del mes
     */
    @GetMapping("/mes/{year}/{month}")
    public ResponseEntity<List<DisponibilidadDiaDTO>> getMes(@PathVariable int year, @PathVariable int month) {
      return ResponseEntity.ok(horarioService.obtenerDisponibilidadMes(year, month));
    }
  }
