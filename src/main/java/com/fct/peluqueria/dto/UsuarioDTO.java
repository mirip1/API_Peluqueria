package com.fct.peluqueria.dto;

import java.beans.JavaBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data tranfer Object de la clase Usuario
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
  private Integer id;
  private String nombre;
  private String apellidos;
  private String email;
  private String telefono;
  private String rol;
  private Boolean baneado;

}