package com.fct.peluqueria.converter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fct.peluqueria.constants.EstadoCita;
import com.fct.peluqueria.constants.Rol;
import com.fct.peluqueria.dto.CitaDTO;
import com.fct.peluqueria.dto.HorarioDTO;
import com.fct.peluqueria.dto.PeluqueriaDTO;
import com.fct.peluqueria.dto.RegistroDTO;
import com.fct.peluqueria.dto.ResenaDTO;
import com.fct.peluqueria.dto.ServicioDTO;
import com.fct.peluqueria.dto.UsuarioDTO;
import com.fct.peluqueria.models.Cita;
import com.fct.peluqueria.models.HorarioBase;
import com.fct.peluqueria.models.HorarioExcepcion;
import com.fct.peluqueria.models.Peluqueria;
import com.fct.peluqueria.models.Resena;
import com.fct.peluqueria.models.Servicio;
import com.fct.peluqueria.models.Usuario;
import com.fct.peluqueria.repository.UsuarioRepository;

/**
 * Clase para realizar conversiones de Objetos
 */
public class ConverterUtil {
  
  public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
  
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
        .fecha(usuario.getFechaCreacion())
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
          .nombreUsuario(resena.getUsuario().getNombre() + " " + resena.getUsuario().getApellidos())
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
  
  /**
   * Convierte un objeto HorarioBase a HorarioDTO.
   * @param el Objeto a convertir
   * @return HorarioDTO relleno
   */
  public static HorarioDTO horarioBaseToHorarioDTO(HorarioBase horario) {
    return HorarioDTO.builder().
        id(horario.getId())
        .diaSemana(horario.getDiaSemana())
        .horaInicio(horario.getHoraInicio() != null ? horario.getHoraInicio().format(TIME_FORMATTER) : null)
        .horaFin(horario.getHoraFin() != null ? horario.getHoraFin().format(TIME_FORMATTER) : null)
        .estado(horario.getEstado() != null ? horario.getEstado() : null).build();
  }
  
  
  /**
   * Convierte un objeto HorarioExcepcion a HorarioDTO.
   * @param el Objeto a convertir
   * @return HorarioDTO relleno
   */
  public static HorarioDTO horarioExcepcionToHorarioDTO(HorarioExcepcion excepcion) {
    return HorarioDTO.builder()
        .id(excepcion.getId()).diaSemana(excepcion.getDiaSemana())
        .horaInicio(excepcion.getHoraInicio() != null ? excepcion.getHoraInicio().format(TIME_FORMATTER) : null)
        .horaFin(excepcion.getHoraFin() != null ? excepcion.getHoraFin().format(TIME_FORMATTER) : null)
        .estado(excepcion.getEstado() != null ? excepcion.getEstado() : null).build();
  }
  
 /**
  * Pasa una cadena que contiene la informaciond e una hora a LocalTime
  * @param timeStr cadena de la hora
  * @return la hora en LocalTime
  */
  public static LocalTime parseLocalTime(String timeStr) {
    return LocalTime.parse(timeStr, TIME_FORMATTER);
  }

}
