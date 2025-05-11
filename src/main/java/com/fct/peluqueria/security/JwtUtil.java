package com.fct.peluqueria.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fct.peluqueria.constants.Rol;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private long expiration;

  /**
   * Genera un token JWT usando el username como subject.
   * 
   * @param username El nombre de usuario para el cual se genera el token.
   * @param role El rol del usuario.
   * @return Un token JWT firmado.
   */
  public String generateToken(String username, Rol role) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);
    Map<String, Object> claims = new HashMap<>();
    claims.put("rol", role);
    return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(now).setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  /**
   * Extrae el username (subject) del token JWT.
   * 
   * @param token El token JWT.
   * @return El username contenido en el token.
   */
  public String extractUsername(String token) {
    return getClaims(token).getSubject();
  }

  /**
   * Valida el token verificando que no haya expirado.
   * 
   * @param token El token JWT.
   * @return true si el token es valido, false en caso contrario.
   */
  public boolean validateToken(String token) {
    try {
      Claims claims = getClaims(token);
      return !claims.getExpiration().before(new Date());
    } catch (Exception ex) {
      return false;
    }
  }

  /**
   * Obtiene las claims (datos) contenidas en el token.
   * 
   * @param token El token JWT.
   * @return Las claims del token.
   */
  private Claims getClaims(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }
}
