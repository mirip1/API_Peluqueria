package com.fct.peluqueria.dto;

import com.fct.peluqueria.constants.DiaSemana;
import com.fct.peluqueria.constants.EstadoHorario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data tranfer Object de la clase Horario
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDTO {
  private Integer id;
  private DiaSemana diaSemana;
  private String horaInicio;
  private String horaFin;
  private EstadoHorario estado;
}