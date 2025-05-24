package com.example.Bio_Code.controlador;

import com.example.Bio_Code.dto.LoginRequest;
import com.example.Bio_Code.modelo.persona;
import com.example.Bio_Code.repositorio.UsuarioRepository;
import com.example.Bio_Code.servicios.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")  // permite Angular local
@RestController
public class UsuarioController {

    @Autowired
    private AuthService authService;

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/usuarios")
    public List<persona> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            persona persona = authService.login(loginRequest.getCorreo(), loginRequest.getContrasena());
            return ResponseEntity.ok(persona);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
