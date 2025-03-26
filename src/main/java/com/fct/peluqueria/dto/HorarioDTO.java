package com.fct.peluqueria.dto;

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
  private String diaSemana;
  private String horaInicio;
  private String horaFin;
  private String estado;
}