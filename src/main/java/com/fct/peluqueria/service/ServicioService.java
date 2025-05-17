package com.fct.peluqueria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.converter.ConverterUtil;
import com.fct.peluqueria.dto.ServicioDTO;
import com.fct.peluqueria.models.Servicio;
import com.fct.peluqueria.repository.ServicioRepository;

@Service
public class ServicioService {

  @Autowired
  private ServicioRepository servicioRepository;
  
  /**
   * Metodo para recoger todos los servicios de la BBDD
   * 
   * @return lista de todos los servicios
   */
  public List<ServicioDTO> getAllServicios() {
    List<Servicio> servicios = servicioRepository.findAll();
    return servicios.stream()
        .map(servicio -> ConverterUtil.servicioToServicioDTO(servicio))
        .collect(Collectors.toList());
  }

  /**
   * Metodo para crear un servicio en la BBDD
   * 
   * @param servicio a añadir
   * @return servicio creado
   */
  public ServicioDTO crearServicio(ServicioDTO servicio) {
    Servicio entidad = new Servicio();
    entidad.setNombre(servicio.getNombre());
    entidad.setPrecio(servicio.getPrecio());
    Servicio guardado = servicioRepository.save(entidad);
    return ConverterUtil.servicioToServicioDTO(guardado);
  }

  /**
   * Metodo para actualizar un servicio
   * 
   * @param id  del servicio a actualizar
   * @param servicio a actualizar
   * @return servicio actualizado
   */
  public ServicioDTO updateServicio(Integer id, ServicioDTO servicio) {
    Servicio entidad = servicioRepository.findById(id)
       .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + id));
    entidad.setNombre(servicio.getNombre());
    entidad.setPrecio(servicio.getPrecio());
    Servicio actualizado = servicioRepository.save(entidad);
    return ConverterUtil.servicioToServicioDTO(actualizado);
  }

  /**
   * Metodo para borrar un servicio
   * 
   * @param id del servicio a borrar
   */
  public void deleteServicio(Integer id) {
    if (!servicioRepository.existsById(id)) {
      throw new RuntimeException("Servicio no encontrado: " + id);
    }
    servicioRepository.deleteById(id);
  }
}