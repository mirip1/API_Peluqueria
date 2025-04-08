package com.fct.peluqueria.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.constants.DiaSemana;
import com.fct.peluqueria.converter.ConverterUtil;
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
     * Obtiene la disponibilidad de horarios para una fecha específica.
     * Se toma el horario base del día de la semana y se aplican las excepciones registradas para esa fecha.
     *
     * @param fecha la fecha para la que se consulta la disponibilidad.
     * @return Lista de HorarioDTO resultante.
     */
    public List<HorarioDTO> obtenerHorariosPorFecha(LocalDate fecha) {
        DayOfWeek dow = fecha.getDayOfWeek();
        DiaSemana diaSemana = convertDayOfWeekToDiaSemana(dow);

        // Obtener horarios base para ese día
        List<HorarioBase> horariosBase = horarioBaseRepository.findByDiaSemana(diaSemana);
        List<HorarioDTO> horarios = horariosBase.stream()
                .map(ConverterUtil::horarioBaseToHorarioDTO)
                .collect(Collectors.toList());

        List<HorarioExcepcion> excepciones = horarioExcepcionRepository.findByFecha(fecha);

        for (HorarioExcepcion excepcion : excepciones) {
            horarios = horarios.stream().map(h -> {
                if (h.getHoraInicio().equals(excepcion.getHoraInicio().format(ConverterUtil.TIME_FORMATTER)) &&
                    h.getHoraFin().equals(excepcion.getHoraFin().format(ConverterUtil.TIME_FORMATTER))) {
                    h.setEstado(excepcion.getEstado());
                }
                return h;
            }).collect(Collectors.toList());
        }

        return horarios;
    }

    /**
     * Actualiza un horario base existente.
     *
     * @param id el identificador del horario base.
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
     * @param fecha la fecha en la que se aplica la excepción.
     * @return el DTO de la excepción creada.
     */
    public HorarioDTO addExcepcionHorario(HorarioDTO excepcionDTO, LocalDate fecha) {
        HorarioExcepcion excepcion = HorarioExcepcion.builder()
                .fecha(fecha)
                .horaInicio(ConverterUtil.parseLocalTime(excepcionDTO.getHoraInicio()))
                .horaFin(ConverterUtil.parseLocalTime(excepcionDTO.getHoraFin()))
                .estado(excepcionDTO.getEstado())
                .diaSemana(excepcionDTO.getDiaSemana())
                .build();

        HorarioExcepcion savedExcepcion = horarioExcepcionRepository.save(excepcion);
        return ConverterUtil.horarioExcepcionToHorarioDTO(savedExcepcion);
    }

    /**
     * Método para convertir de dayOfWeek a enum DiaSemana.
     */
    private DiaSemana convertDayOfWeekToDiaSemana(java.time.DayOfWeek dow) {
        switch(dow) {
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
