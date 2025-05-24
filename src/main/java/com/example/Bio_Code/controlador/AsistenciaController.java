package com.example.Bio_Code.controlador;

import com.example.Bio_Code.repositorio.AsistenciaRepository;
import com.example.Bio_Code.modelo.Control_Asistencia;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")  // permite Angular local
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
}

