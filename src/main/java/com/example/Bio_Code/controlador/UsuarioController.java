package com.example.Bio_Code.controlador;

import com.example.Bio_Code.dto.LoginRequest;
import com.example.Bio_Code.dto.PersonaDTO;
import com.example.Bio_Code.modelo.Ficha;
import com.example.Bio_Code.modelo.Persona;
import com.example.Bio_Code.modelo.Rol;
import com.example.Bio_Code.modelo.Tipo_documento;
import com.example.Bio_Code.repositorio.FichaRepository;
import com.example.Bio_Code.repositorio.RolRepository;
import com.example.Bio_Code.repositorio.TipoDocumentoRepository;
import com.example.Bio_Code.repositorio.UsuarioRepository;
import com.example.Bio_Code.servicios.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")  // permite Angular local
@RestController
public class UsuarioController {

    @Autowired
    private AuthService authService;

    private final UsuarioRepository usuarioRepository;

    @Autowired
    private FichaRepository fichaRepository;


    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Listar todos los usuarios
    @GetMapping("/usuarios")
    public List<Persona> listarUsuarios() {
        return usuarioRepository.findByEstado(true);
    }

    // Obtener usuario por id
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Persona> obtenerUsuarioPorId(@PathVariable Integer id) {
        Optional<Persona> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }




    @PostMapping("/usuariosCrear")
    public Persona crearUsuario(@RequestBody PersonaDTO dto) {
        Persona persona = new Persona();
        persona.setNombres(dto.nombres);
        persona.setApellidos(dto.apellidos);
        persona.setNo_documento(dto.no_documento);
        persona.setCorreo(dto.correo);
        persona.setTelefono(dto.telefono);
        persona.setEstado(true); // Siempre lo fuerzas a true
        persona.setContrasena(dto.contrasena);

        // Decodificar la foto Base64 (si viene)
        if (dto.foto != null && !dto.foto.isEmpty()) {
            try {
                byte[] fotoBytes = Base64.getDecoder().decode(dto.foto);
                persona.setFoto(fotoBytes);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("La foto no tiene un formato Base64 válido");
            }
        }

        // Validar y asignar relaciones
        Rol rol = rolRepository.findById(dto.id_rol)
                .orElseThrow(() -> new RuntimeException("Rol con id " + dto.id_rol + " no encontrado"));

// Validar ficha solo si se requiere
        Ficha ficha = null;
        if (dto.id_ficha != null) {
            ficha = fichaRepository.findById(dto.id_ficha)
                    .orElseThrow(() -> new RuntimeException("Ficha con id " + dto.id_ficha + " no encontrada"));
        }

        Tipo_documento tipoDocumento = tipoDocumentoRepository.findById(dto.id_tipo_documento)
                .orElseThrow(() -> new RuntimeException("TipoDocumento con id " + dto.id_tipo_documento + " no encontrado"));

        persona.setRol(rol);
        persona.setFicha(ficha); // Puede ser null
        persona.setTipo_documento(tipoDocumento);


        return usuarioRepository.save(persona);
    }



    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Persona persona = authService.login(loginRequest.getCorreo(), loginRequest.getContrasena());
            return ResponseEntity.ok(persona);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    // Listar todas las competencias
    @GetMapping("/ficha")
    public List<Ficha> listarFicha() {
        return fichaRepository.findAll();
    }

    @PutMapping("/usuariActualizar/{id}")
    public Persona actualizarUsuario(@PathVariable Integer id, @RequestBody PersonaDTO dto) {
        Persona persona = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona con ID " + id + " no encontrada"));

        // Actualizar campos básicos
        persona.setNombres(dto.nombres);
        persona.setApellidos(dto.apellidos);
        persona.setNo_documento(dto.no_documento);
        persona.setCorreo(dto.correo);
        persona.setTelefono(dto.telefono);
        persona.setEstado(dto.estado);
        persona.setContrasena(dto.contrasena);

        // Actualizar foto si viene
        if (dto.foto != null && !dto.foto.isEmpty()) {
            try {
                byte[] fotoBytes = Base64.getDecoder().decode(dto.foto);
                persona.setFoto(fotoBytes);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("La foto no tiene un formato Base64 válido");
            }
        }

        // Actualizar relaciones
        Rol rol = rolRepository.findById(dto.id_rol)
                .orElseThrow(() -> new RuntimeException("Rol con id " + dto.id_rol + " no encontrado"));

        Tipo_documento tipoDocumento = tipoDocumentoRepository.findById(dto.id_tipo_documento)
                .orElseThrow(() -> new RuntimeException("TipoDocumento con id " + dto.id_tipo_documento + " no encontrado"));

        persona.setRol(rol);
        persona.setTipo_documento(tipoDocumento);

        // Ficha solo si se envía (puede ser null)
        if (dto.id_ficha != null) {
            Ficha ficha = fichaRepository.findById(dto.id_ficha)
                    .orElseThrow(() -> new RuntimeException("Ficha con id " + dto.id_ficha + " no encontrada"));
            persona.setFicha(ficha);
        } else {
            persona.setFicha(null); // Si no se envía ficha, la elimina
        }

        return usuarioRepository.save(persona);
    }

        @PutMapping("/inhabilitarPersona/{id}")
        public ResponseEntity<?> inhabilitarPersona(@PathVariable Integer id) {
            Optional<Persona> personaOpt = usuarioRepository.findById(id);

            if (personaOpt.isPresent()) {
                Persona persona = personaOpt.get();
                persona.setEstado(false);  // Cambiar estado a inactivo
                usuarioRepository.save(persona);
                return ResponseEntity.ok("Persona inhabilitada correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Persona no encontrada");
            }
        }
    }

