package com.example.Bio_Code.controlador;

import com.example.Bio_Code.dto.ExportFiltroDTO;
import com.example.Bio_Code.dto.FichaDTO;
import com.example.Bio_Code.dto.PersonaConAsistenciaDTO;
import com.example.Bio_Code.modelo.Rol;
import com.example.Bio_Code.modelo.Tipo_documento;
import com.example.Bio_Code.repositorio.RolRepository;
import com.example.Bio_Code.repositorio.TipoDocumentoRepository;
import com.example.Bio_Code.servicios.ExportService;
import com.example.Bio_Code.servicios.FichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuarios/fichas")
@CrossOrigin(origins = "http://localhost:4200") // ajusta si tu frontend está en otro puerto
public class FichaController {

    @Autowired
    private FichaService fichaService;
    @Autowired
    private ExportService exportService;

    @GetMapping
    public List<FichaDTO> listarFichas() {
        return fichaService.obtenerFichas();
    }
    @Autowired
    private TipoDocumentoRepository repo;
    @GetMapping("/tiposDocumento")
    public List<Tipo_documento> getTiposDocumento() {
        return repo.findAll();
    }
    @Autowired
    private RolRepository rolRepository;

    @GetMapping("/roles")
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }
    @PostMapping("/excel")
    public ResponseEntity<byte[]> exportarExcel(@RequestBody ExportFiltroDTO filtros) {
        List<PersonaConAsistenciaDTO> datosFiltrados = exportService.obtenerDatosFiltradosConAsistencia(filtros);
        byte[] excelBytes = exportService.generarExcel(datosFiltrados);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("reporte.xlsx").build());

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }


}
