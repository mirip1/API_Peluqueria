package com.fct.peluqueria.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fct.peluqueria.constants.EstadoCita;
import com.fct.peluqueria.constants.Rol;
import com.fct.peluqueria.dto.CitaDTO;
import com.fct.peluqueria.dto.PeluqueriaDTO;
import com.fct.peluqueria.dto.RegistroDTO;
import com.fct.peluqueria.dto.ResenaDTO;
import com.fct.peluqueria.dto.ServicioDTO;
import com.fct.peluqueria.dto.UsuarioDTO;
import com.fct.peluqueria.models.Cita;
import com.fct.peluqueria.models.Peluqueria;
import com.fct.peluqueria.models.Resena;
import com.fct.peluqueria.models.Servicio;
import com.fct.peluqueria.models.Usuario;
import com.fct.peluqueria.repository.UsuarioRepository;

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
  
  /**
   * Convierte de Peluqueria a PeluqueriaDTO
   * @param peluqueria el Objeto a convertir
   * @return PeluqueriaDTO relleno
   */
  public static PeluqueriaDTO peluqueriaToPeluqueriaDTO(Peluqueria peluqueria) {
    return PeluqueriaDTO.builder()
        .id(peluqueria.getId())
        .nombre(peluqueria.getNombre())
        .descripcion(peluqueria.getDescripcion())
        .ubicacion(peluqueria.getUbicacion())
        .build();
    
  }
  
  /**
   * Convierte de Servicio a PeluqueriaDTO
   * @param servicio el Objeto a convertir
   * @return ServicioDTO relleno
   */
  public static ServicioDTO servicioToServicioDTO(Servicio servicio) {
    return ServicioDTO.builder()
        .id(servicio.getId())
        .nombre(servicio.getNombre())
        .precio(servicio.getPrecio())
        .build();

  }
  
  
  /**
   * Convierte de Resena a ResenaDTO
   * @param resena el Objeto a convertir
   * @return ResenaDTO relleno
   */
  public static ResenaDTO resenaToResenaDTO(Resena resena) {
      return ResenaDTO.builder()
          .id(resena.getId())
          .usuarioId(resena.getUsuario().getId())
          .comentario(resena.getComentario())
          .puntuacion(resena.getPuntuacion())
          .fecha(resena.getFecha())
          .build();
  }
  
  /**
   * Convierte de Resena a ResenaDTO
   * 
   * @param resena el Objeto a convertir
   * @return ResenaDTO relleno
   */
  public static Resena ResenaDTOToresena(ResenaDTO dto) {
    LocalDateTime fechaActual = LocalDateTime.now(ZoneId.of("CET"));
    Resena resena = Resena.builder()
        .comentario(dto.getComentario())
        .puntuacion(dto.getPuntuacion())
        .fecha(fechaActual)
        .build();

    return resena;
  }
  
  /**
   * Convierte un objeto Cita a CitaDTO.
   * @param cita el Objeto a convertir
   * @return CitaDTO relleno
   */
  public static CitaDTO citaToCitaDTO(Cita cita) {
    return CitaDTO.builder()
          .id(cita.getId())
          .fechaYHora(cita.getFechaYHora())
          .estado(cita.getEstado() != null ? cita.getEstado().name() : null)
          .usuarioId(cita.getUsuario().getId())
          .build();
  }

  /**
   * Convierte un objeto CitaDTO a Cita.
   * @param dto el Objeto a convertir
   * @return Cita relleno
   */
  public static Cita citaDTOToCita(CitaDTO dto, UsuarioRepository usuarioRepository) {
    Cita cita = new Cita();
    cita.setId(dto.getId());
    cita.setFechaYHora(dto.getFechaYHora() != null ? dto.getFechaYHora() : LocalDateTime.now());
    if (dto.getEstado() != null) {
      System.out.println(EstadoCita.valueOf(dto.getEstado().toUpperCase()));
      cita.setEstado(EstadoCita.valueOf(dto.getEstado().toUpperCase()));
    }
    if (dto.getUsuarioId() != null) {
      Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
          .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + dto.getUsuarioId()));
      cita.setUsuario(usuario);

    }
    return cita;
  }

}
