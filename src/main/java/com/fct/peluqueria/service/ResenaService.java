package com.fct.peluqueria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.converter.ConverterUtil;
import com.fct.peluqueria.dto.ResenaDTO;
import com.fct.peluqueria.models.Resena;
import com.fct.peluqueria.repository.ResenaRepository;
import com.fct.peluqueria.repository.UsuarioRepository;

@Service
public class ResenaService {

  @Autowired
  private ResenaRepository resenaRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  /**
   * Obtiene todas las reseñas ordenadas por fecha
   */
  public List<ResenaDTO> getAllResenas() {
    List<Resena> resenas = resenaRepository.findAll();

    return resenas.stream().map(resena -> ConverterUtil.resenaToResenaDTO(resena)).collect(Collectors.toList());
  }

  /**
   * Agrega una nueva reseña a la base de datos.
   */
  public ResenaDTO addResena(ResenaDTO resenaDTO) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Resena resena = convertToEntity(resenaDTO, auth);
    Resena savedResena = resenaRepository.save(resena);
    return ConverterUtil.resenaToResenaDTO(savedResena);
  }


  /**
   * Convierte el ResenaDTO en un Resena y coge el usuario del contexto (Token de Autentificación)
   * @param dto
   * @param auth
   * @return re
   */
  private Resena convertToEntity(ResenaDTO dto, Authentication auth) {
    Resena resena = ConverterUtil.ResenaDTOToresena(dto);

    if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
      UserDetails userDetails = (UserDetails) auth.getPrincipal();
      usuarioRepository.findByEmail(userDetails.getUsername()).ifPresent(resena::setUsuario);
    }
    return resena;
  }
}