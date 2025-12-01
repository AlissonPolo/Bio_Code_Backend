package com.example.Bio_Code.controlador;

import com.example.Bio_Code.repositorio.AsistenciaRepository;
import com.example.Bio_Code.modelo.Control_Asistencia;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    @PutMapping("/asistencias/marcarEstancia/{idPersona}")
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
        } else {
            // Si es otro estado, actualizar la descripción que viene del cliente
            asistencia.setDescripcion(asistenciaActualizada.getDescripcion());
            asistenciaRepository.save(asistencia);
        }

        return ResponseEntity.ok(asistencia);
    }

    @PostMapping("/asistencias/CrearAsistencia")
    public ResponseEntity<Control_Asistencia> crearAsistencia(@RequestBody Control_Asistencia nuevaAsistencia) {
        Control_Asistencia asistenciaGuardada = asistenciaRepository.save(nuevaAsistencia);
        return new ResponseEntity<>(asistenciaGuardada, HttpStatus.CREATED);
    }

    @GetMapping("/asistencias/persona/{id}")
    public List<Control_Asistencia> obtenerAsistenciasPorPersona(@PathVariable Integer id) {
        return asistenciaRepository.findByPersona_Idpersona(id);
    }

    @PutMapping("/asistencias/excusa/{idAsistencia}")
    public ResponseEntity<?> actualizarExcusa(
            @PathVariable int idAsistencia,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @RequestParam(value = "descripcion", required = false) String descripcion) {

        System.out.println("Llega petición PUT /asistencias/excusa/" + idAsistencia);
        if (archivo != null) {
            System.out.println("Archivo recibido: " + archivo.getOriginalFilename() + " | size: " + archivo.getSize());
        }

        try {
            Optional<Control_Asistencia> asistenciaOpt = asistenciaRepository.findById(idAsistencia);
            if (!asistenciaOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la asistencia");
            }

            Control_Asistencia asistencia = asistenciaOpt.get();

            if (archivo != null && !archivo.isEmpty()) {
                asistencia.setDocumento_excusa(archivo.getBytes());
                asistencia.setTipoArchivo(archivo.getContentType());
            }

            if (descripcion != null) {
                asistencia.setDescripcion(descripcion);
            }

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



    @GetMapping("/asistencias/{id}/excusa-descargar")
    public ResponseEntity<Resource> descargarExcusa(@PathVariable int id) {
        Optional<Control_Asistencia> asistenciaOpt = asistenciaRepository.findById(id);

        if (asistenciaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Control_Asistencia asistencia = asistenciaOpt.get();

        byte[] archivo = asistencia.getDocumento_excusa();

        if (archivo == null || archivo.length == 0) {
            return ResponseEntity.noContent().build();
        }

        ByteArrayResource recurso = new ByteArrayResource(archivo);

        String tipoArchivo = asistencia.getTipoArchivo() != null ? asistencia.getTipoArchivo() : "application/octet-stream";

        // Decide extensión del archivo para el nombre de descarga según tipo MIME
        String extension = "dat";
        if (tipoArchivo.contains("pdf")) extension = "pdf";
        else if (tipoArchivo.contains("excel") || tipoArchivo.contains("spreadsheet")) extension = "xlsx";

        String nombreArchivo = "excusa_asistencia_" + asistencia.getIdasistencia() + "." + extension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(tipoArchivo))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .body(recurso);
    }


    @GetMapping("/asistencias/persona/{id}/hoy")
    public ResponseEntity<?> obtenerAsistenciaDeHoy(@PathVariable Integer id) {
        try {
            // Calculas inicio y fin del día (como ya haces)
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date inicio = calendar.getTime();

            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            Date fin = calendar.getTime();

            List<Control_Asistencia> asistencias = asistenciaRepository.findAsistenciaHoy(id, inicio, fin);

            if (!asistencias.isEmpty()) {
                Control_Asistencia asistencia = asistencias.get(0);  // primer resultado
                return ResponseEntity.ok(asistencia);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay asistencia para hoy");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }


    @PutMapping("/asistencias/persona/{id}/hoy")
    public ResponseEntity<?> actualizarAsistenciaDeHoy(@PathVariable Integer id, @RequestBody Control_Asistencia datos) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date inicio = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date fin = calendar.getTime();

        List<Control_Asistencia> asistencias = asistenciaRepository.findAsistenciaHoy(id, inicio, fin);

        Optional<Control_Asistencia> asistenciaOpt = asistencias.isEmpty()
                ? Optional.empty()
                : Optional.of(asistencias.get(0));

        if (asistenciaOpt.isPresent()) {
            Control_Asistencia asistencia = asistenciaOpt.get();

            // Campos que se pueden actualizar (ajusta si necesitas otros)
            asistencia.setEstancia(datos.isEstancia());
            asistencia.setNovedad(datos.getNovedad());
            asistencia.setDescripcion(datos.getDescripcion());
            asistencia.setDocumento_excusa(datos.getDocumento_excusa());
            asistencia.setTipoArchivo(datos.getTipoArchivo());

            asistenciaRepository.save(asistencia);
            return ResponseEntity.ok(asistencia);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró asistencia de hoy para actualizar.");
        }
    }


}
