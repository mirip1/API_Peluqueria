package com.fct.peluqueria.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fct.peluqueria.dto.PeluqueriaDTO;
import com.fct.peluqueria.service.PeluqueriaService;

/**
 * Controlador de Peluqueria
 */
@RestController
@RequestMapping("/api/peluqueria")
public class PeluqueriaController {

    @Autowired
    private PeluqueriaService peluqueriaService;

    /**
     * Metodo para ver la información de la peluqueria
     * @return respuesta de la llamada
     */
    @GetMapping
    public ResponseEntity<?> getPeluqueria() {
        return ResponseEntity.ok(peluqueriaService.getPeluqueria());
    }


    /**
     * Metodo para actualizar la informacion de la peluqueria
     * @param peluqueriaDTO JSON con la informacion de la peluqueria
     * @return la respuesta de la llamada
     */
    @PutMapping
    public ResponseEntity<?> updatePeluqueria(@RequestBody PeluqueriaDTO peluqueriaDTO) {
        return ResponseEntity.ok(peluqueriaService.updatePeluqueria(peluqueriaDTO));
    }
}