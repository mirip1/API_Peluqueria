package com.fct.peluqueria.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
  @NotBlank(message = "El comentario es obligatorio")
  @Size(min = 10, max = 500, message = "La reseña debe tener entre 10 y 500 caracteres")
  @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúñÑ\\s\\.,;:!\\?\"'()\\-]+$", message = "La reseña solo puede contener letras, números y puntuación básica")
  private String comentario;
  private Integer puntuacion;
  private LocalDateTime fecha;
  private String nombreUsuario;

}
