package com.example.Bio_Code.servicios.impl;
import com.example.Bio_Code.dto.PersonaDTO;
import com.example.Bio_Code.repositorio.UsuarioRepository;
import com.example.Bio_Code.servicios.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<PersonaDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream().map(persona -> {
            PersonaDTO dto = new PersonaDTO();
            dto.nombres = persona.getNombres();
            dto.apellidos = persona.getApellidos();
            dto.no_documento = persona.getNo_documento();
            dto.correo = persona.getCorreo();
            dto.telefono = persona.getTelefono();
            dto.estado = persona.getEstado();
            dto.contrasena = persona.getContrasena();

            if (persona.getFoto() != null) {
                dto.foto = Base64.getEncoder().encodeToString(persona.getFoto());
            }
            if (persona.getRol() != null) {
                dto.nombre_rol = persona.getRol().getNombre();
            }
            if (persona.getFicha() != null) {
                dto.codigo_ficha = persona.getFicha().getCodigo();
            }
            if (persona.getTipo_documento() != null) {
                dto.nombre_tipo_documento = persona.getTipo_documento().getDescripcion();
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PersonaDTO> obtenerFiltradosPorTexto(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            return obtenerTodos();
        }

        String filtroMinuscula = filtro.toLowerCase();

        return usuarioRepository.findAll().stream()
                .map(persona -> {
                    PersonaDTO dto = new PersonaDTO();
                    dto.nombres = persona.getNombres();
                    dto.apellidos = persona.getApellidos();
                    dto.no_documento = persona.getNo_documento();
                    dto.correo = persona.getCorreo();
                    dto.telefono = persona.getTelefono();
                    dto.estado = persona.getEstado();
                    dto.contrasena = persona.getContrasena();

                    if (persona.getFoto() != null) {
                        dto.foto = Base64.getEncoder().encodeToString(persona.getFoto());
                    }
                    if (persona.getRol() != null) {
                        dto.nombre_rol = persona.getRol().getNombre();
                    }
                    if (persona.getFicha() != null) {
                        dto.codigo_ficha = persona.getFicha().getCodigo();
                    }
                    if (persona.getTipo_documento() != null) {
                        dto.nombre_tipo_documento = persona.getTipo_documento().getDescripcion();
                    }

                    return dto;
                })
                .filter(dto ->
                        (dto.nombres != null && dto.nombres.toLowerCase().contains(filtroMinuscula)) ||
                                (dto.apellidos != null && dto.apellidos.toLowerCase().contains(filtroMinuscula)) ||
                                (dto.no_documento != null && dto.no_documento.toLowerCase().contains(filtroMinuscula)) ||
                                (dto.correo != null && dto.correo.toLowerCase().contains(filtroMinuscula)) ||
                                (dto.telefono != null && dto.telefono.toString().toLowerCase().contains(filtroMinuscula)) ||
                                (dto.nombre_rol != null && dto.nombre_rol.toLowerCase().contains(filtroMinuscula)) ||
                                (dto.codigo_ficha != null && dto.codigo_ficha.toLowerCase().contains(filtroMinuscula)) ||
                                (dto.nombre_tipo_documento != null && dto.nombre_tipo_documento.toLowerCase().contains(filtroMinuscula))
                )
                .collect(Collectors.toList());
    }
}
