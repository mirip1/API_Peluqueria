package com.fct.peluqueria.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      // Extraer el token del header Authorization
      String authHeader = request.getHeader("Authorization");
      String token = null;
      String username = null;

      logger.info("Procesando solicitud para: " + request.getRequestURI());

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7); // Eliminar "Bearer "
        logger.info("Token JWT encontrado");

        try {
          username = jwtUtil.extractUsername(token);
          logger.info("Username extraído del token: " + username);
        } catch (Exception e) {
          logger.error("Error al extraer username del token: " + e.getMessage());
        }
      } else {
        logger.info("No se encontró token JWT en la solicitud");
      }

      // Verificar que el token sea válido y que no haya autenticación previa
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        try {
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          logger.info("Usuario cargado: " + userDetails.getUsername());

          if (jwtUtil.validateToken(token)) {
            logger.info("Token JWT válido");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Autenticación establecida en el SecurityContextHolder");
          } else {
            logger.warn("Token JWT inválido");
          }
        } catch (Exception e) {
          logger.error("Error al cargar el usuario o validar el token: " + e.getMessage());
        }
      }
    } catch (Exception e) {
      logger.error("Error en la autenticación JWT: " + e.getMessage());
    }

    filterChain.doFilter(request, response);
  }
}