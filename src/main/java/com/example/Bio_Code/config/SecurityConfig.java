package com.example.Bio_Code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration

public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/usuariosCrear",
                                "/usuarios",
                                "/usuarios/**",
                                "/usuariActualizar/**",
                                "/inhabilitarPersona/**",
                                "/asistencias/{idPersona}/excusa",
                                "/asistencias/persona/**",
                                "/CrearAsistencia",
                                "/marcarEstancia/**",
                                "/asistencia",
                                "/fichas/**",
                                "/tipos-documento",
                                "/roles",
                                "/"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}