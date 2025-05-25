package com.fct.peluqueria.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fct.peluqueria.constants.Rol;
import com.fct.peluqueria.converter.ConverterUtil;
import com.fct.peluqueria.dto.ChangeEmailDTO;
import com.fct.peluqueria.dto.ChangePasswordDTO;
import com.fct.peluqueria.dto.LoginDTO;
import com.fct.peluqueria.dto.RegistroDTO;
import com.fct.peluqueria.dto.UsuarioDTO;
import com.fct.peluqueria.models.Usuario;
import com.fct.peluqueria.repository.UsuarioRepository;
import com.fct.peluqueria.security.JwtUtil;
import com.fct.peluqueria.service.notifications.NotificationService;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
  
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  
  private static final SecureRandom RANDOM = new SecureRandom();
  
  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtil jwtUtil;
  
  @Autowired
  private NotificationService notifier;


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
    
    if(usuario.getBaneado()) {
      throw new RuntimeException("Usuario baneado");
    }

    if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
      throw new RuntimeException("Credenciales incorrectas");
    }

    // Generar token JWT. Aquí usamos el email como subject.
    String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol());

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

  /**
   * Metodo para cambiar la contraseña de un Usuario
   * 
   * @param email email del usuario a cambiar
   * @param dto   deto con la antigua contraseña y la nueva
   */
  @Transactional
  public void changePassword(String email, ChangePasswordDTO dto) {
    Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    if (!passwordEncoder.matches(dto.getOldPassword(), usuario.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña antigua incorrecta");
    }
    usuario.setPassword(passwordEncoder.encode(dto.getNewPassword()));
  }

  /**
   * Metodo para cambiar el email de un usuario
   * 
   * @param email actual del usuario
   * @param dto   informacion con el email nuevo a cambiar
   * @return el usuario con el email cambiado
   */
  @Transactional
  public UsuarioDTO changeEmail(String email, ChangeEmailDTO dto) {
    if (usuarioRepository.existsByEmail(dto.getNewEmail())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email en uso");
    }
    Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    if (!dto.getNewEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
      throw new IllegalArgumentException("El correo electrónico no es válido.");
    }
    usuario.setEmail(dto.getNewEmail());
    return ConverterUtil.usuarioToUsuarioDTO(usuario);
  }

  /**
   * Metodo para borrar un Usuario
   * 
   * @param email el email del usuario a eliminar
   */
  @Transactional
  public void deleteAccount(String email) {
    Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    usuarioRepository.delete(usuario);
  }

  /**
   * Metodo para cambiar el telefono
   * 
   * @param email
   * @param telefono nuevo a cambiar
   */
  @Transactional
  public void changeTelefono(String email, String telefono) {
    Usuario u = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

    String nuevo = telefono;
    if (nuevo == null || !nuevo.matches("^[679]\\d{8}$")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "El teléfono debe tener 9 dígitos y comenzar por 6, 7 o 9.");
    }
    u.setTelefono(nuevo);
  }
  
  /**
   * Método que devuelve Todos los usuarios de la BBDD
   * 
   * @return
   */
  public List<UsuarioDTO> listAllUsuarios() {
    return usuarioRepository.findByRol(Rol.CLIENTE).stream().map(ConverterUtil::usuarioToUsuarioDTO).toList();
  }

  /**
   * 
   * @param id
   * @throws NotFoundException
   */
  @Transactional
  public void banearUsuarioById(Integer id) throws NotFoundException {
    Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException());
    u.setBaneado(true);
  }

  /**
   * metodo que desbanea a un ususario
   * @param id
   * @throws NotFoundException
   */
  @Transactional
  public void desbanearUsuarioById(Integer id) throws NotFoundException {
    Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException());
    u.setBaneado(false);
  }
  
  /**
   * Metodo para cambiar la contraseña y 
   * @param email
   */
  @Transactional
  public void resetearPasswordEmail(String email) {
      Usuario u = usuarioRepository.findByEmail(email)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email no registrado"));
      String nuevoPass = generateRandom(12);

      u.setPassword(passwordEncoder.encode(nuevoPass));
      usuarioRepository.save(u);
      notifier.sendResetPasswordEmail(u, nuevoPass);
  }

  /**
   * Método que comprueba si la informacion de un Usuario es valida
   * 
   * @param usuario usuario a comprobar
   */
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
    if (!usuario.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_-€#]).{8,}$")) {
      throw new IllegalArgumentException(
          "La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula, una minúscula, un número y un carácter especial.");
    }

  }
  
  /**
   * Genera una cadena aleatoria de longitud dada usando caracteres alfanuméricos.
   * @param length longitud de la cadena a generar
   * @return cadena aleatoria
   */
  public static String generateRandom(int length) {
      StringBuilder sb = new StringBuilder(length);
      for (int i = 0; i < length; i++) {
          int idx = RANDOM.nextInt(CHARACTERS.length());
          sb.append(CHARACTERS.charAt(idx));
      }
      return sb.toString();
  }

}
