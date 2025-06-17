package com.example.Bio_Code.repositorio;

import com.example.Bio_Code.modelo.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Persona, Integer> {
    Optional<Persona> findByCorreo(String correo);
    List<Persona> findByEstado(boolean estado);
}
