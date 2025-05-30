package com.example.Bio_Code.controlador;

import com.example.Bio_Code.dto.LoginRequest;
import com.example.Bio_Code.modelo.persona;
import com.example.Bio_Code.repositorio.UsuarioRepository;
import com.example.Bio_Code.servicios.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")  // permite Angular local
@RestController
public class UsuarioController {

    @Autowired
    private AuthService authService;

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Listar todos los usuarios
    @GetMapping("/usuarios")
    public List<persona> listarUsuarios() {
        return usuarioRepository.findByEstado(true);
    }

    // Obtener usuario por id
    @GetMapping("/usuarios/{id}")
    public ResponseEntity <persona> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<persona> usuario = usuarioRepository.findById(String.valueOf(id));
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Crear un nuevo usuario (CREATE)
    @PostMapping("/usuariosCrear")
    public persona crearUsuario(@RequestBody persona nuevoUsuario) {
        return usuarioRepository.save(nuevoUsuario);
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
