package com.fct.peluqueria.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fct.peluqueria.constants.EstadoCita;
import com.fct.peluqueria.converter.ConverterUtil;
import com.fct.peluqueria.dto.CitaDTO;
import com.fct.peluqueria.models.Cita;
import com.fct.peluqueria.models.Usuario;
import com.fct.peluqueria.repository.CitaRepository;
import com.fct.peluqueria.repository.UsuarioRepository;
import com.fct.peluqueria.service.notifications.NotificationService;

import jakarta.transaction.Transactional;

@Service
public class CitaService {

  @Autowired
  private CitaRepository citaRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;
  
  @Autowired
  private NotificationService notifier;

  /**
   * Crea una nueva cita a partir del CitaDTO recibido. Si no se especifica el
   * estado, se asigna por defecto ACTIVA.
   *
   * @param citaDTO objeto de transferencia con la informacion necesaria.
   * @return El CitaDTO resultante tras guardar la cita.
   */
  public CitaDTO crearCita(CitaDTO citaDTO) {
    // Si ya existe un acita con las mismas características pero cancelada la
    // eliminamos
    citaRepository
        .findByUsuarioIdAndFechaYHoraAndEstado(citaDTO.getUsuarioId(), citaDTO.getFechaYHora(), EstadoCita.CANCELADA)
        .ifPresent(citaCancelada -> {
          citaRepository.delete(citaCancelada);
        });
    Cita cita = ConverterUtil.citaDTOToCita(citaDTO, usuarioRepository);
    if (cita.getEstado() == null) {
      cita.setEstado(EstadoCita.ACTIVA);
    }
    Cita savedCita = citaRepository.save(cita);
    return ConverterUtil.citaToCitaDTO(savedCita);
  }

  /**
   * Obtiene la lista de citas asociadas a un usuario.
   *
   * @param usuarioId Identificador del usuario.
   * @return Lista de CitaDTO correspondientes a las citas del usuario.
   */
  public List<CitaDTO> obtenerCitasPorUsuario(Integer usuarioId) {
    List<Cita> citas = citaRepository.findByUsuarioId(usuarioId);
    return citas.stream().map(ConverterUtil::citaToCitaDTO).collect(Collectors.toList());
  }

  /**
   * Cancela una cita dado su id. Se marca la cita con el estado CANCELADA.
   *
   * @param id Identificador de la cita a cancelar.
   */
  public void cancelarCita(Integer id) {
    Cita cita = citaRepository.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    cita.setEstado(EstadoCita.CANCELADA);
    citaRepository.save(cita);
    notifier.sendCancellationNotice(cita);
  }

  /**
   * Elimina una cita de la BBDD
   * 
   * @param citaId cita a eliminar
   * @param email  email de la persona que tiene la cita
   */
  public void eliminarCita(Integer citaId, String email) {
    Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    Cita cita = citaRepository.findById(citaId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (!cita.getUsuario().getId().equals(usuario.getId())) {
      throw new AccessDeniedException("No puedes borrar esta cita");
    }
    if (!cita.getEstado().equals(EstadoCita.ACTIVA)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo citas activas pueden borrarse");
    }
    citaRepository.delete(cita);
  }

  /**
   * Recoge todas las citas existentes
   * 
   * @return lista de todas las citas
   */
  public List<CitaDTO> getAllCitas() {
    return citaRepository.findAll().stream().map(ConverterUtil::citaToCitaDTO).collect(Collectors.toList());
  }

//  @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Madrid")
  @Scheduled(fixedRate = 300000)
  @Transactional
  public void finalizarCitasVencidas() {
    LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Madrid"));
    List<Cita> pendientes = citaRepository.findAllByEstado(EstadoCita.ACTIVA);
    for (Cita c : pendientes) {
      if (c.getFechaYHora().isBefore(now)) {
        c.setEstado(EstadoCita.FINALIZADA);
      }
    }
  }
}
