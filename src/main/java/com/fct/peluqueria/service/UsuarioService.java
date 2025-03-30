package com.fct.peluqueria.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.converter.ConverterUtil;
import com.fct.peluqueria.dto.LoginDTO;
import com.fct.peluqueria.dto.RegistroDTO;
import com.fct.peluqueria.dto.UsuarioDTO;
import com.fct.peluqueria.models.Usuario;
import com.fct.peluqueria.repository.UsuarioRepository;
import com.fct.peluqueria.security.JwtUtil;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private JwtUtil jwtUtil;

//  @Autowired
//  private JwtUtil jwtUtil;

  /**
   * Metodo que se encarga de la lógica detras de un registro
   * 
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

  /**
   * Valida las credenciales del usuario, si son correctas devuelve un token JWT
   * 
   * @param loginDTO
   * @return
   */
  public Object login(LoginDTO loginDTO) {
    // Buscar usuario por email
    Optional<Usuario> optUsuario = usuarioRepository.findByEmail(loginDTO.getEmail());
    if (optUsuario.isEmpty()) {
      throw new RuntimeException("Usuario no encontrado");
    }

    Usuario usuario = optUsuario.get();

    if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
      throw new RuntimeException("Credenciales incorrectas");
    }

    // Generar token JWT. Aquí usamos el email como subject.
    String token = jwtUtil.generateToken(usuario.getEmail());

    return token;
  }

  /**
   * Metodo que devuelve los datos de un usuario ya autetificado
   * @param email
   * @return
   */
  public UsuarioDTO getProfile(String email) {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    UsuarioDTO usuarioDTO = ConverterUtil.usuarioToUsuarioDTO(usuario);

    return usuarioDTO;
  }

}
