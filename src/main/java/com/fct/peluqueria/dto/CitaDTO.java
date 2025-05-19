package com.fct.peluqueria.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data tranfer Object de la clase Cita
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {
  private Integer id;
  private Integer usuarioId;
  private LocalDateTime fechaYHora;
  private String estado;
  private String usuarioEmail;
}
