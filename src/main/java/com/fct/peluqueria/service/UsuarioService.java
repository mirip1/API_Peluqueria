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
    checkUsuario(usuario);
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
  public String login(LoginDTO loginDTO) {
    // Seguridad
    if (!loginDTO.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
      throw new RuntimeException("Usuario no encontrado");
    }

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
   * 
   * @param email
   * @return
   */
  public UsuarioDTO getProfile(String email) {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    UsuarioDTO usuarioDTO = ConverterUtil.usuarioToUsuarioDTO(usuario);

    return usuarioDTO;
  }

  private void checkUsuario(Usuario usuario) {
    if (usuario == null) {
      throw new IllegalArgumentException("El usuario no puede ser nulo.");

    }
    // Teléfono
    if (!usuario.getTelefono().matches("^[679]\\d{8}$")) {
      throw new IllegalArgumentException("El número de teléfono debe tener 9 dígitos y comenzar por 6, 7 o 9.");
    }

    // Nombre: Solo letras (incluye tildes y mayúscula inicial)
    if (!usuario.getNombre().matches("^[A-ZÁÉÍÓÚÑ][a-záéíóúñ]{1,29}$")) {
      throw new IllegalArgumentException("El nombre debe comenzar con mayúscula y solo contener letras.");
    }

    // Apellidos: Puede ser uno o dos, misma regla que nombre
    if (!usuario.getApellidos().matches("^[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+(\\s[A-ZÁÉÍÓÚÑ][a-záéíóúñ]+)?$")) {
      throw new IllegalArgumentException(
          "Los apellidos deben comenzar con mayúscula y solo contener letras, uno o dos apellidos permitidos.");
    }

    // Email
    if (!usuario.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
      throw new IllegalArgumentException("El correo electrónico no es válido.");
    }

    // Password: mínimo 8 caracteres, una mayúscula, una minúscula, un número y un
    // carácter especial
    if (!usuario.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
      throw new IllegalArgumentException(
          "La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula, una minúscula, un número y un carácter especial.");
    }

  }

}
