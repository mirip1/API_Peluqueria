package com.fct.peluqueria.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fct.peluqueria.constants.Rol;
import com.fct.peluqueria.dto.RegistroDTO;
import com.fct.peluqueria.dto.UsuarioDTO;
import com.fct.peluqueria.models.Usuario;

/**
 * Clase para realizar conversiones de Objetos
 */
public class ConverterUtil {
  
  /**
   * Convierte del RegistroDTO a usuario, pero sin la contraseña
   * @param registroDTO el Objeto a convertir
   * @return usuario con relleno
   */
  public static Usuario registroDTOToUsuario(RegistroDTO registroDTO) {
    LocalDateTime fechaActual = LocalDateTime.now(ZoneId.of("CET"));
    return Usuario.builder()
        .nombre(registroDTO.getNombre())
        .apellidos(registroDTO.getApellidos())
        .email(registroDTO.getEmail())
        .telefono(registroDTO.getTelefono())
        .rol(Rol.CLIENTE)
        .baneado(false)
        .fechaCreacion(fechaActual)
        .build();
  }

  /**
   * Convierte de Usuario a UsuarioDTO
   * @param usuario el Objeto a convertir
   * @return usuarioDTO relleno
   */
  public static UsuarioDTO usuarioToUsuarioDTO(Usuario usuario) {
    return UsuarioDTO.builder()
        .id(usuario.getId())
        .nombre(usuario.getNombre())
        .apellidos(usuario.getApellidos())
        .email(usuario.getEmail())
        .telefono(usuario.getTelefono())
        .rol(usuario.getRol())
        .baneado(usuario.getBaneado())
        .build();
  }

}
