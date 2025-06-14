package com.fct.peluqueria.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.constants.DiaSemana;
import com.fct.peluqueria.converter.ConverterUtil;
import com.fct.peluqueria.dto.DisponibilidadDiaDTO;
import com.fct.peluqueria.dto.HorarioDTO;
import com.fct.peluqueria.models.HorarioBase;
import com.fct.peluqueria.models.HorarioExcepcion;
import com.fct.peluqueria.repository.HorarioBaseRepository;
import com.fct.peluqueria.repository.HorarioExcepcionRepository;

@Service
public class HorarioService {

  @Autowired
  private HorarioBaseRepository horarioBaseRepository;

  @Autowired
  private HorarioExcepcionRepository horarioExcepcionRepository;

  /**
   * Obtiene la disponibilidad de horarios para una fecha específica. Se toma el
   * horario base del día de la semana y se aplican las excepciones registradas
   * para esa fecha.
   *
   * @param fecha la fecha para la que se consulta la disponibilidad.
   * @return Lista de HorarioDTO resultante.
   */
  public List<HorarioDTO> obtenerHorariosPorFecha(LocalDate fecha) {
    DiaSemana diaSemana = convertDayOfWeekToDiaSemana(fecha.getDayOfWeek());
    List<HorarioDTO> base = horarioBaseRepository.findByDiaSemana(diaSemana).stream()
        .map(ConverterUtil::horarioBaseToHorarioDTO).collect(Collectors.toList());
    List<HorarioDTO> excepciones = horarioExcepcionRepository.findByFecha(fecha).stream()
        .map(ConverterUtil::horarioExcepcionToHorarioDTO).collect(Collectors.toList());
    if (!excepciones.isEmpty()) {
      // Sólo excepciones
      excepciones.sort(Comparator.comparing(HorarioDTO::getHoraInicio));
      return excepciones;
    }
    List<HorarioDTO> resultado = base.stream()
        .filter(b -> excepciones.stream()
            .noneMatch(e -> e.getHoraInicio().equals(b.getHoraInicio()) && e.getHoraFin().equals(b.getHoraFin())))
        .collect(Collectors.toList());
    resultado.addAll(excepciones);
    resultado.sort(Comparator.comparing(HorarioDTO::getHoraInicio));
    return resultado;
  }

  /**
   * Actualiza un horario base existente.
   *
   * @param id         el identificador del horario base.
   * @param horarioDTO los nuevos datos para el horario.
   * @return el horario base actualizado en forma de DTO.
   */
  public HorarioDTO updateHorarioBase(Integer id, HorarioDTO horarioDTO) {
    HorarioBase horario = horarioBaseRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Horario base no encontrado con id: " + id));

    horario.setDiaSemana(horarioDTO.getDiaSemana());
    horario.setHoraInicio(ConverterUtil.parseLocalTime(horarioDTO.getHoraInicio()));
    horario.setHoraFin(ConverterUtil.parseLocalTime(horarioDTO.getHoraFin()));
    if (horarioDTO.getEstado() != null) {
      horario.setEstado(horarioDTO.getEstado());
    }
    HorarioBase updated = horarioBaseRepository.save(horario);
    return ConverterUtil.horarioBaseToHorarioDTO(updated);
  }

  /**
   * Agrega una excepción de horario para una fecha específica.
   *
   * @param excepcionDTO el DTO con la información de la excepción.
   * @param fecha        la fecha en la que se aplica la excepción.
   * @return el DTO de la excepción creada.
   */
  public HorarioDTO addExcepcionHorario(HorarioDTO excepcionDTO, LocalDate fecha) {
    HorarioExcepcion excepcion = HorarioExcepcion.builder().fecha(fecha)
        .horaInicio(ConverterUtil.parseLocalTime(excepcionDTO.getHoraInicio()))
        .horaFin(ConverterUtil.parseLocalTime(excepcionDTO.getHoraFin())).estado(excepcionDTO.getEstado())
        .diaSemana(excepcionDTO.getDiaSemana()).build();

    HorarioExcepcion savedExcepcion = horarioExcepcionRepository.save(excepcion);
    return ConverterUtil.horarioExcepcionToHorarioDTO(savedExcepcion);
  }

  /**
   * Devuelve la la lista disponibilidad de cada deia de un mes en especifico
   * 
   * @param year  año
   * @param month mes
   * @return lista de disponibilidad por dia del mes
   */
  public List<DisponibilidadDiaDTO> obtenerDisponibilidadMes(int year, int month) {
    List<DisponibilidadDiaDTO> mes = new ArrayList<>();
    YearMonth ym = YearMonth.of(year, month);
    for (int day = 1; day <= ym.lengthOfMonth(); day++) {
      LocalDate fecha = ym.atDay(day);
      // reutilizamos la lógica ya corregida que filtra excepciones
      List<HorarioDTO> franjas = obtenerHorariosPorFecha(fecha);
      DisponibilidadDiaDTO dto = new DisponibilidadDiaDTO();
      dto.setFecha(fecha);
      dto.setFranjas(franjas);
      mes.add(dto);
    }
    return mes;
  }

  /**
   * Obtiene el horario base de la BBDD
   * 
   * @return horario semanal base
   */
  public List<HorarioDTO> obtenerHorarioBase() {
    return horarioBaseRepository.findAllOrderedByDiaSemana().stream().map(ConverterUtil::horarioBaseToHorarioDTO)
        .collect(Collectors.toList());
  }

  /**
   * Crea un intervalo de horario base
   * 
   * @param dto
   * @return el dto del nuevo intervalo
   */
  public HorarioDTO createHorarioBase(HorarioDTO dto) {
    HorarioBase hb = ConverterUtil.horarioDTOToHorarioBase(dto);
    HorarioBase saved = horarioBaseRepository.save(hb);
    return ConverterUtil.horarioBaseToHorarioDTO(saved);
  }

  /**
   * Metodo que borra un intervalo en horario base
   * 
   * @param id
   */
  public void deleteHorarioBase(Integer id) {
    horarioBaseRepository.deleteById(id);
  }

  /**
   * Metodo que borra un intervalo en horario excepcion
   * 
   * @param id
   */
  public void deleteExcepcion(Integer id) {
    horarioExcepcionRepository.deleteById(id);
  }

  /**
   * Método para convertir de dayOfWeek a enum DiaSemana.
   */
  private DiaSemana convertDayOfWeekToDiaSemana(java.time.DayOfWeek dow) {
    switch (dow) {
    case MONDAY:
      return DiaSemana.LUNES;
    case TUESDAY:
      return DiaSemana.MARTES;
    case WEDNESDAY:
      return DiaSemana.MIERCOLES;
    case THURSDAY:
      return DiaSemana.JUEVES;
    case FRIDAY:
      return DiaSemana.VIERNES;
    case SATURDAY:
      return DiaSemana.SABADO;
    case SUNDAY:
      return DiaSemana.DOMINGO;
    default:
      throw new IllegalArgumentException("No existe ningun dia llamado: " + dow);
    }
  }
}
