package com.fct.peluqueria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data tranfer Object de la clase ChangeEmailDTO
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeEmailDTO {
  private String newEmail;
}
