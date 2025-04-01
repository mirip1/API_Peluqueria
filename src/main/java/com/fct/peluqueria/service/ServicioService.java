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

  public List<ServicioDTO> getAllServicios() {
    List<Servicio> servicios = servicioRepository.findAll();
    return servicios.stream()
        .map(servicio -> ConverterUtil.servicioToServicioDTO(servicio))
        .collect(Collectors.toList());
  }
}
