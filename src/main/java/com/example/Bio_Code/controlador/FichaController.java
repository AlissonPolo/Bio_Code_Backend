package com.example.Bio_Code.controlador;

import com.example.Bio_Code.dto.FichaDTO;
import com.example.Bio_Code.modelo.Rol;
import com.example.Bio_Code.modelo.Tipo_documento;
import com.example.Bio_Code.repositorio.RolRepository;
import com.example.Bio_Code.repositorio.TipoDocumentoRepository;
import com.example.Bio_Code.servicios.FichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fichas")
@CrossOrigin(origins = "http://localhost:4200") // ajusta si tu frontend está en otro puerto
public class FichaController {

    @Autowired
    private FichaService fichaService;

    @GetMapping
    public List<FichaDTO> listarFichas() {
        return fichaService.obtenerFichas();
    }
    @Autowired
    private TipoDocumentoRepository repo;
    @GetMapping("/tipos-documento")
    public List<Tipo_documento> getTiposDocumento() {
        return repo.findAll();
    }
    @Autowired
    private RolRepository rolRepository;

    @GetMapping("/roles")
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

}
