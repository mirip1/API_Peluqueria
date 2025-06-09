package com.fct.peluqueria.service.notifications;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.constants.EstadoCita;
import com.fct.peluqueria.models.Cita;
import com.fct.peluqueria.models.Usuario;
import com.fct.peluqueria.repository.CitaRepository;

/**
 * Servicio que gestiona las notificaciones
 */
@Service
public class NotificationService {

  @Autowired
  private CitaRepository citaRepository;

//  @Autowired
//  private WhatsappNotifier whatsappNotifier;

  @Autowired
  private EmailNotifier emailNotifier;

  /**
   * Metodo que comprueba a las 8 de la mañana todos los dias y manda un correo
   * a los clientes que tienen una cita activa al dia siguiente
   */
  @Scheduled(cron = "0 0 8 * * *", zone = "Europe/Madrid")
//  @Scheduled(fixedRate = 300000)
  public void sendTomorrowReminders() {
    LocalDate manana = LocalDate.now(ZoneId.of("Europe/Madrid")).plusDays(1);
    String subject = "Recordatorio de cita";
    // buscar citas activas para mañana
    List<Cita> citasManana = citaRepository.findAllByEstadoAndFechaYHoraBetween(EstadoCita.ACTIVA,
        manana.atStartOfDay(), manana.plusDays(1).atStartOfDay());

    for (Cita c : citasManana) {
      String email = c.getUsuario().getEmail();
      String hora = c.getFechaYHora().toLocalTime().toString();
      String fecha = c.getFechaYHora().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
      String mensaje = String.format("¡Hola %s! Te recordamos tu cita mañana %s a las %s. ¡Te esperamos!",
          c.getUsuario().getNombre(), fecha, hora);
      emailNotifier.sendEmail(email, subject, mensaje);
    }
  }

  /**
   * Metodo que avisa al cliente de una cancelación de una cita
   * 
   * @param cita cancelada
   */
  public void sendCancellationNotice(Cita cita) {
    String email = cita.getUsuario().getEmail();
    String subject = "Cancelacion de cita";
    String mensaje = String.format(
        "Hola %s, lamentamos informar que su cita del %s ha sido CANCELADA por el peluquero.",
        cita.getUsuario().getNombre(), cita.getFechaYHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    emailNotifier.sendEmail(email, subject, mensaje);

  }

  /**
   * Metodo que manda un correo con la contraseña reseteada a los clientes que se
   * le ha olvidad la contraseña
   * 
   * @param u cliente
   * @param nuevoPass nueva contraseña
   */
  public void sendResetPasswordEmail(Usuario u, String nuevoPass) {
    String subject = "Recuperación de contraseña";
    String body = String.format(
        "Hola %s,\n\nTu contraseña ha sido regenerada. Tu nueva contraseña es:\n\n%s\n\nPor favor, cámbiala tras iniciar sesión.",
        u.getNombre(), nuevoPass);
    emailNotifier.sendEmail(u.getEmail(), subject, body);
  }
}
