package com.fct.peluqueria.web;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fct.peluqueria.dto.ChangeEmailDTO;
import com.fct.peluqueria.dto.ChangeEmailResponse;
import com.fct.peluqueria.dto.ChangePasswordDTO;
import com.fct.peluqueria.dto.LoginDTO;
import com.fct.peluqueria.dto.RegistroDTO;
import com.fct.peluqueria.dto.UsuarioDTO;
import com.fct.peluqueria.security.JwtUtil;
import com.fct.peluqueria.service.UsuarioService;

/**
 * Controller de Usuario
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;
  
  @Autowired
  private JwtUtil jwtUtil;

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
    Map<String, String> response = new HashMap<>();
    response.put("token", usuarioService.login(loginDTO));
    return ResponseEntity.ok(response);
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

  /**
   * Metodo que cambia la contraseña actual de un Usuario, recibe la nueva
   * contraseña y la antigua
   */
  @PutMapping("/password")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto, Principal principal) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      // Obtén el nombre de usuario (email) del objeto Authentication
      String username = authentication.getName();
      usuarioService.changePassword(username, dto);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
    }

  }

  /**
   * Metodo para cambiar el email de un usuario
   */
  @PutMapping("/email")
  public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailDTO dto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      // Obtén el nombre de usuario (email) del objeto Authentication
      UsuarioDTO updated = usuarioService.changeEmail(authentication.getName(), dto);
      String newToken = jwtUtil.generateToken(updated.getEmail(), updated.getRol());
      return ResponseEntity.ok(new ChangeEmailResponse(updated, newToken));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
  }

  /**
   * Metodo para eliminar un Usuario
   */
  @DeleteMapping
  public ResponseEntity<?> deleteAccount() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    usuarioService.deleteAccount(authentication.getName());
    return ResponseEntity.noContent().build();
  }

  /**
   * Metodo para cambiar el telefono
   */
  @PutMapping("/telefono")
  public ResponseEntity<?> changeTelefono(@RequestBody String telefono) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      // Obtén el nombre de usuario (email) del objeto Authentication
      usuarioService.changeTelefono(authentication.getName(), telefono);
      return ResponseEntity.ok(null);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
  }
  
  /**
   * metodo para cambiar la contraseña a alguien que se le ha olvidado
   * @param email
   * @return
   */
  @PostMapping("/forgot-password")
  public ResponseEntity<Void> forgotPassword(@RequestParam("email") String email) {
      usuarioService.resetearPasswordEmail(email);
      return ResponseEntity.noContent().build();
  }
  
}