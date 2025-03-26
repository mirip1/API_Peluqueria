package com.fct.peluqueria.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fct.peluqueria.dto.ResenaDTO;
import com.fct.peluqueria.service.ResenaService;

/**
 * Controller de Resena
 */
@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    /**
     * Metodo para coger la lista de Reseñas
     * @return respuesta de la llamada
     */
    @GetMapping
    public ResponseEntity<?> getResenas() {
        return ResponseEntity.ok(resenaService.getAllResenas());
    }

    /**
     * Añade reseña
     * @param resenaDTO Json de reseña a añadir
     * @return respuesta de la llamada
     */
    @PostMapping
    public ResponseEntity<?> addResena(@RequestBody ResenaDTO resenaDTO) {
        return ResponseEntity.ok(resenaService.addResena(resenaDTO));
    }
}
