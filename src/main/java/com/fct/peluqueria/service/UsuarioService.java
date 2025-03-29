package com.fct.peluqueria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.converter.ConverterUtil;
import com.fct.peluqueria.dto.LoginDTO;
import com.fct.peluqueria.dto.RegistroDTO;
import com.fct.peluqueria.dto.UsuarioDTO;
import com.fct.peluqueria.models.Usuario;
import com.fct.peluqueria.repository.UsuarioRepository;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

//  @Autowired
//  private JwtUtil jwtUtil;

  /**
   * Metodo que se encarga de la lógica detras de un registro
   * @param registroDTO
   * @return usuario ya registrado (sin la contraseña)
   */
  public UsuarioDTO register(RegistroDTO registroDTO) {

    Usuario usuario = ConverterUtil.registroDTOToUsuario(registroDTO);
    usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
    usuarioRepository.save(usuario);

    // Convertimos el Usuario a UsuarioDTO (sin la contraseña)
    UsuarioDTO usuarioDTO = ConverterUtil.usuarioToUsuarioDTO(usuario);

    return usuarioDTO;

  }

  public Object login(LoginDTO loginDTO) {
    return null;
  }

  public UsuarioDTO getProfile(String email) {
    return null;
  }

}
