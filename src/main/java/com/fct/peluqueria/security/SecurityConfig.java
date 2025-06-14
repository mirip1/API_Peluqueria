package com.fct.peluqueria.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Autowired
  @Lazy
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
    .cors(Customizer.withDefaults())   
    .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.GET, "/api/peluqueria").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/resenas").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/usuarios/forgot-password").permitAll()
        .requestMatchers("/api/usuarios/**").permitAll().anyRequest().authenticated())
        .formLogin(form -> form.usernameParameter("email").defaultSuccessUrl("/api/usuarios/profile", true))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }
  
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowedOrigins(List.of(
        "http://localhost:4200",
        "https://mivel.eu",
        "https://www.mivel.eu"
    ));
    cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
    cfg.setAllowedHeaders(List.of("*"));
    cfg.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
    src.registerCorsConfiguration("/**", cfg);
    return src;
  }


}
