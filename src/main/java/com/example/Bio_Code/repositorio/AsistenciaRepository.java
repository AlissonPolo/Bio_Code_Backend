package com.example.Bio_Code.repositorio;

import com.example.Bio_Code.modelo.Control_Asistencia;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Control_Asistencia, String> {
    List<Control_Asistencia> findByPersona_Idpersona(Integer idpersona);

    @Modifying
    @Transactional
    @Query("UPDATE Control_Asistencia c SET c.descripcion = '', c.documento_excusa = null WHERE c.idasistencia = :id")
    void limpiarExcusa(@Param("id") Integer idAsistencia);


}







