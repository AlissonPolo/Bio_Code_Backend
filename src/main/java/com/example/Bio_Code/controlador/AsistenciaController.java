package com.example.Bio_Code.controlador;

import com.example.Bio_Code.repositorio.AsistenciaRepository;
import com.example.Bio_Code.modelo.Control_Asistencia;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
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


    @Transactional
    @PutMapping("/marcarEstancia/{idPersona}")
    public ResponseEntity<Control_Asistencia> actualizarEstancia(
            @PathVariable int idPersona,
            @RequestBody Control_Asistencia asistenciaActualizada) {

        List<Control_Asistencia> asistencias = asistenciaRepository.findByPersona_Idpersona(idPersona);

        if (asistencias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Control_Asistencia asistencia = asistencias.get(0);

        asistencia.setEstancia(asistenciaActualizada.isEstancia());

        String novedad = asistenciaActualizada.getNovedad();
        asistencia.setNovedad(novedad);

        if ("asistido".equalsIgnoreCase(novedad) || "asistió".equalsIgnoreCase(novedad)) {
            asistenciaRepository.limpiarExcusa(asistencia.getIdasistencia());
        }
        else {
            // Si es otro estado, actualizar la descripción que viene del cliente
            asistencia.setDescripcion(asistenciaActualizada.getDescripcion());
            asistenciaRepository.save(asistencia);
        }

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

    @PostMapping("/asistencias/{idPersona}/excusa")
    public ResponseEntity<?> subirExcusa(
            @PathVariable int idPersona,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @RequestParam(value = "descripcion", required = false) String descripcion) {

        try {
            List<Control_Asistencia> asistencias = asistenciaRepository.findByPersona_Idpersona(idPersona);
            if (asistencias.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró asistencia para el usuario");
            }

            Control_Asistencia asistencia = asistencias.get(0);

            if (archivo != null && !archivo.isEmpty()) {
                asistencia.setDocumento_excusa(archivo.getBytes());
            }
            asistencia.setDescripcion(descripcion != null ? descripcion : asistencia.getDescripcion());

            // Si quieres actualizar novedad aquí, debes recibirla y setearla también.

            asistenciaRepository.save(asistencia);

            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Excusa guardada correctamente");
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la excusa: " + e.getMessage());
        }
    }



}

