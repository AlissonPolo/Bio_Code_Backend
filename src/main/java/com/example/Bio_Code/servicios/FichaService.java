package com.example.Bio_Code.servicios;

import com.example.Bio_Code.dto.FichaDTO;
import com.example.Bio_Code.repositorio.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FichaService {

    @Autowired
    private FichaRepository fichaRepository;

    public List<FichaDTO> obtenerFichas() {
        return fichaRepository.findAll().stream()
                .map(ficha -> new FichaDTO(ficha.getId_ficha(), ficha.getCodigo()))
                .collect(Collectors.toList());
    }


}
