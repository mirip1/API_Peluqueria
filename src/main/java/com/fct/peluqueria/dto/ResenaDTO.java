package com.fct.peluqueria.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data tranfer Object de la clase Resena
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResenaDTO {
  private Integer id;
  private Integer usuarioId;
  private String comentario;
  private Integer puntuacion;
  private LocalDateTime fecha;
  private String nombreUsuario;

}
