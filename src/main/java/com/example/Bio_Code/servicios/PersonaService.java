package com.example.Bio_Code.servicios;

import com.example.Bio_Code.dto.PersonaDTO;
import java.util.List;

public interface PersonaService {
    List<PersonaDTO> obtenerTodos();
    List<PersonaDTO> obtenerFiltradosPorTexto(String filtro);

}
