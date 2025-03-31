package com.fct.peluqueria.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fct.peluqueria.dto.LoginDTO;
import com.fct.peluqueria.dto.RegistroDTO;
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
   * 
   * @param usuarioDTO JSON del usuario a añadir
   * @return respuesta de la llamada
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegistroDTO register) {
    return ResponseEntity.ok(usuarioService.register(register));
  }

  /**
   * Metodo para iniciar sesión a un usuario existente
   * 
   * @param loginDTO JSON del login (datos necesarios para iniciar sesion)
   * @return respuesta de la llamada
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
    return ResponseEntity.ok(usuarioService.login(loginDTO));
  }

  /**
   * Metodo obtiene el perfil de usuario ya autentificado
   * 
   * @param principal
   * @return
   */
  @GetMapping("/profile")
  public ResponseEntity<?> profile(Principal principal) {
    // Obtén la autenticación del contexto de seguridad
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      // Obtén el nombre de usuario (email) del objeto Authentication
      String username = authentication.getName();

      // Obtén el perfil del usuario usando el servicio
      UsuarioDTO usuarioDTO = usuarioService.getProfile(username);
      return ResponseEntity.ok(usuarioDTO);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
    }
  }
}