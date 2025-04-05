package com.fct.peluqueria.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.constants.EstadoHorario;
import com.fct.peluqueria.converter.ConverterUtil;
import com.fct.peluqueria.dto.HorarioDTO;
import com.fct.peluqueria.models.Horario;
import com.fct.peluqueria.repository.HorarioRepository;

@Service
public class HorarioService {

  @Autowired
  private HorarioRepository horarioRepository;

  /**
   * Obtiene la lista de horarios para un día específico.
   *
   * @param diaSemana El día de la semana .
   * @return Lista de HorarioDTO para ese día.
   */
  public List<HorarioDTO> getHorariosPorDia(String diaSemana) {
    // Convertimos el día a mayúsculas para asegurar coincidencia con la BBDD
    List<Horario> horarios = horarioRepository.findByDiaSemana(diaSemana.toUpperCase());
    return horarios.stream().map(ConverterUtil::horarioToHorarioDTO).collect(Collectors.toList());
  }

  /**
   * Actualiza un horario existente.
   *
   * @param id         El identificador del horario a actualizar.
   * @param horarioDTO Los nuevos datos del horario.
   * @return El HorarioDTO actualizado.
   */
  public HorarioDTO updateHorario(Integer id, HorarioDTO horarioDTO) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    Horario horario = horarioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Horario no encontrado con id: " + id));
    
    if (horarioDTO.getDiaSemana() != null) {
      horario.setDiaSemana(horarioDTO.getDiaSemana());
    }
    if (horarioDTO.getHoraInicio() != null) {
      horario.setHoraInicio(LocalTime.parse(horarioDTO.getHoraInicio(), formatter));
    }
    if (horarioDTO.getHoraFin() != null) {
      horario.setHoraFin(LocalTime.parse(horarioDTO.getHoraFin(), formatter));
    }
    if (horarioDTO.getEstado() != null) {
      horario.setEstado(horarioDTO.getEstado());
    }
    Horario updated = horarioRepository.save(horario);
    return ConverterUtil.horarioToHorarioDTO(updated);
  }

}
