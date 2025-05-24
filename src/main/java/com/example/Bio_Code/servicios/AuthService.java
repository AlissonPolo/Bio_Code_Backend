package com.example.Bio_Code.servicios;

import com.example.Bio_Code.modelo.persona;
import com.example.Bio_Code.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public persona login(String correo, String contrasena) {
        Optional<persona> personaOpt = usuarioRepository.findByCorreo(correo);
        if (personaOpt.isPresent()) {
            persona persona = personaOpt.get();
            if (persona.getContrasena().equals(contrasena)) {
                return persona;
            } else {
                throw new RuntimeException("Contraseña incorrecta");
            }
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }
}
