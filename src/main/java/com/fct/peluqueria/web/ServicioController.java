package com.fct.peluqueria.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @return respuesta de la llamada
     */
    @GetMapping
    public ResponseEntity<?> getServicios() {
        return ResponseEntity.ok(servicioService.getAllServicios());
    }
}