package com.fct.peluqueria.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fct.peluqueria.dto.LoginDTO;
import com.fct.peluqueria.dto.UsuarioDTO;
import com.fct.peluqueria.service.UsuarioService;

/**
 * Controller de Usuario
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Metodo para registrarse
     * @param usuarioDTO JSON del usuario a añadir
     * @return respuesta de la llamada
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.register(usuarioDTO));
    }
    
    
    /**
     * Metodo para iniciar sesión a un usuario existente
     * @param loginDTO JSON del login (datos necesarios para iniciar sesion)
     * @return respuesta de la llamada
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(usuarioService.login(loginDTO));
    }
    
    
    /**
     * Metodo obtiene el perdil de usuario ya autentificado
     * @param principal
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity<?> profile(Principal principal) {
        return ResponseEntity.ok(usuarioService.getProfile(principal.getName()));
    }
}