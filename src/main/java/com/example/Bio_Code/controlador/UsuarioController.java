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
import com.example.Bio_Code.servicios.PersonaService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200", "https://biocode-production.up.railway.app"}, allowCredentials = "true")
@RestController
public class UsuarioController {
    @Autowired
    private PersonaService personaService;

    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;
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




    @PostMapping("usuarios/usuariosCrear")
    public Persona crearUsuario(@RequestBody PersonaDTO dto) {
        Persona persona = new Persona();
        persona.setNombres(dto.nombres);
        persona.setApellidos(dto.apellidos);
        persona.setNo_documento(dto.no_documento);
        persona.setCorreo(dto.correo);
        persona.setTelefono(dto.telefono);
        persona.setEstado(true);
        persona.setContrasena(passwordEncoder.encode(dto.contrasena));


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
    @GetMapping("/usuarios/ficha")
    public List<Ficha> listarFicha() {
        return fichaRepository.findAll();
    }

    @PutMapping("usuarios/usuariActualizar/{id}")
    public Persona actualizarUsuario(@PathVariable Integer id, @RequestBody PersonaDTO dto) {
        Persona persona = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona con ID " + id + " no encontrada"));

        // Actualizar campos básicosx
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

        @PutMapping("usuarios/inhabilitarPersona/{id}")
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

    @GetMapping("/exportarExcel")
    public void exportarUsuariosExcel(
            @RequestParam(required = false) String filtro,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.xlsx");

        List<PersonaDTO> usuarios;

        if (filtro != null && !filtro.trim().isEmpty()) {
            // Llama a un método de tu servicio que filtre por texto
            usuarios = personaService.obtenerFiltradosPorTexto(filtro.trim().toLowerCase());
        } else {
            usuarios = personaService.obtenerTodos();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios");

        // Estilo para encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 11);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // Estilo para celdas normales
        CellStyle normalStyle = workbook.createCellStyle();
        normalStyle.setWrapText(true);

        // Crear encabezados
        String[] columnas = {
                "Nombres", "Apellidos", "Tipo Documento", "Documento",
                "Correo", "Teléfono", "Estado", "Rol", "Ficha"
        };

        Row header = sheet.createRow(0);
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (PersonaDTO usuario : usuarios) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(usuario.nombres);
            row.createCell(1).setCellValue(usuario.apellidos);
            row.createCell(2).setCellValue(usuario.nombre_tipo_documento != null ? usuario.nombre_tipo_documento : "");
            row.createCell(3).setCellValue(usuario.no_documento);
            row.createCell(4).setCellValue(usuario.correo);
            row.createCell(5).setCellValue(usuario.telefono != null ? usuario.telefono.toString() : "");
            row.createCell(6).setCellValue(Boolean.TRUE.equals(usuario.estado) ? "Activo" : "Inactivo");
            row.createCell(7).setCellValue(usuario.nombre_rol != null ? usuario.nombre_rol : "");
            row.createCell(8).setCellValue(usuario.codigo_ficha != null ? usuario.codigo_ficha : "");

            for (int i = 0; i < columnas.length; i++) {
                row.getCell(i).setCellStyle(normalStyle);
            }
        }

        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }


}


