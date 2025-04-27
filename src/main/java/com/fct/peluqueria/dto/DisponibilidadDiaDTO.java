package com.fct.peluqueria.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data tranfer Object de la clase Disponibilidad
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilidadDiaDTO {
  private LocalDate fecha;
  private List<HorarioDTO> franjas;

}
