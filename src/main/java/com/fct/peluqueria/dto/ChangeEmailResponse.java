package com.fct.peluqueria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response de la llamda de cambiar el email
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeEmailResponse {
  private UsuarioDTO usuario;
  private String token;

}
