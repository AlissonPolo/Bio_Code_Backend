package com.example.Bio_Code.controlador;

import com.example.Bio_Code.repositorio.AsistenciaRepository;
import com.example.Bio_Code.modelo.Control_Asistencia;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController

public class AsistenciaController {
    private final AsistenciaRepository asistenciaRepository;

    public AsistenciaController(AsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }
    @GetMapping("/asistencia")
    public List<Control_Asistencia> listarasistencia() {
        return asistenciaRepository.findAll();
    }

    @PutMapping("/marcarEstancia/{idPersona}")
    public ResponseEntity<Control_Asistencia> actualizarEstancia(
            @PathVariable int idPersona,
            @RequestBody Control_Asistencia asistenciaActualizada) {

        List<Control_Asistencia> asistencias = asistenciaRepository.findByPersona_Idpersona(idPersona);

        if (asistencias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Tomar la primera asistencia encontrada
        Control_Asistencia asistencia = asistencias.get(0);

        // Actualizar campos con los valores recibidos
        asistencia.setEstancia(asistenciaActualizada.isEstancia());

        // Actualizar novedad (suponiendo que es String o enum)
        asistencia.setNovedad(asistenciaActualizada.getNovedad());

        // Si hay más campos que quieras actualizar, agrégalos aquí

        asistenciaRepository.save(asistencia);

        return ResponseEntity.ok(asistencia);
    }



    @PostMapping("/CrearAsistencia")
    public ResponseEntity<Control_Asistencia> crearAsistencia(@RequestBody Control_Asistencia nuevaAsistencia) {
        Control_Asistencia asistenciaGuardada = asistenciaRepository.save(nuevaAsistencia);
        return new ResponseEntity<>(asistenciaGuardada, HttpStatus.CREATED);
    }
    @GetMapping("/asistencias/persona/{id}")
    public List<Control_Asistencia> obtenerAsistenciasPorPersona(@PathVariable Integer id) {
        return asistenciaRepository.findByPersona_Idpersona(id);
    }


}

