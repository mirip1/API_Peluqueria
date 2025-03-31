package com.fct.peluqueria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.converter.ConverterUtil;
import com.fct.peluqueria.dto.PeluqueriaDTO;
import com.fct.peluqueria.models.Peluqueria;
import com.fct.peluqueria.repository.PeluqueriaRepository;

@Service
public class PeluqueriaService {

  @Autowired
  private PeluqueriaRepository peluqueriaRepository;

  /**
   * Devuelve la información de la peluqueria. (solo debe haber un registro)
   */
  public PeluqueriaDTO getPeluqueria() {
    Peluqueria peluqueria = peluqueriaRepository.findFirst();
    if (peluqueria == null) {
      throw new RuntimeException("No se encontró la peluqueria");
    }
    return ConverterUtil.peluqueriaToPeluqueriaDTO(peluqueria);
  }

  /**
   * Actualiza la información de la peluqueria. Solo se permite a usuarios con rol
   * ADMIN realizar esta operación.
   */
  public PeluqueriaDTO updatePeluqueria(PeluqueriaDTO peluqueriaDTO) {
    Peluqueria peluqueria = peluqueriaRepository.findById(peluqueriaDTO.getId())
        .orElseThrow(() -> new RuntimeException("Peluqueria no encontrada"));

    peluqueria.setNombre(peluqueriaDTO.getNombre());
    peluqueria.setDescripcion(peluqueriaDTO.getDescripcion());
    peluqueria.setUbicacion(peluqueriaDTO.getUbicacion());

    Peluqueria updated = peluqueriaRepository.save(peluqueria);
    return ConverterUtil.peluqueriaToPeluqueriaDTO(updated);
  }


}
