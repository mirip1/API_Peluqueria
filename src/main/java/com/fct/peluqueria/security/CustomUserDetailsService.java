package com.fct.peluqueria.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fct.peluqueria.models.Usuario;
import com.fct.peluqueria.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      Usuario usuario = usuarioRepository.findByEmail(username)
          .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

      List<GrantedAuthority> authorities;
      if ("Rol.ADMIN".equalsIgnoreCase(usuario.getRol().toString())) {
//        System.out.println("llega hasta aqui :D");
        authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
      } else {
        authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
      }
      return new User(usuario.getEmail(), usuario.getPassword(), authorities);
    } catch (Exception e) {
      throw new UsernameNotFoundException("Error al cargar usuario: " + e.getMessage());
    }
  }
}