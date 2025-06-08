package com.example.Bio_Code.repositorio;

import com.example.Bio_Code.modelo.Control_Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Control_Asistencia, String> {
    List<Control_Asistencia> findByPersona_Idpersona(Integer idpersona);

}







