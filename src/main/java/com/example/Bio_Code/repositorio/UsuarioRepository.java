
package com.example.Bio_Code.repositorio;

import com.example.Bio_Code.modelo.persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<persona, String> {
    Optional<persona> findByCorreo(String correo);

    List<persona> findByEstado(boolean estado);
}
