package com.fct.peluqueria.constants;

/**
 * Enum de Roles de usuario
 */
public enum Rol {
  CLIENTE, 
  ADMIN;

  public static Rol fromString(String valor) {
    return Rol.valueOf(valor.toUpperCase());
  }
}