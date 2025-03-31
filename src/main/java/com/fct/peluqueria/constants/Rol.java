package com.fct.peluqueria.constants;

import lombok.ToString;

/**
 * Enum de Roles de usuario
 */
@ToString
public enum Rol {
  CLIENTE, 
  ADMIN;

  public static Rol fromString(String valor) {
    return Rol.valueOf(valor.toUpperCase());
  }
}