package com.fct.peluqueria.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fct.peluqueria.constants.DiaSemana;
import com.fct.peluqueria.dto.HorarioDTO;
import com.fct.peluqueria.service.HorarioService;

/**
 * Controller de horario
 */
@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    
    /**
     * @param dia dia de la semana
     * @return respuesta de la llamada
     */
    @GetMapping("/{dia}")
    public ResponseEntity<?> getHorariosPorDia(@PathVariable("dia") DiaSemana dia) {
        return ResponseEntity.ok(horarioService.getHorariosPorDia(dia));
    }

    /**
     * @param id identificador del horario
     * @param horarioDTO JSON con el horario actualizado
     * @return respuesta de la llamada
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHorario(@PathVariable Integer id, @RequestBody HorarioDTO horarioDTO) {
        return ResponseEntity.ok(horarioService.updateHorario(id, horarioDTO));
    }
}