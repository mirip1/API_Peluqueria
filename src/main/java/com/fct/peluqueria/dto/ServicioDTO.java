package com.fct.peluqueria.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/**
 * Data tranfer Object de la clase Servicio
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServicioDTO {
  private Integer id;
  private String nombre;
  private BigDecimal precio;

}