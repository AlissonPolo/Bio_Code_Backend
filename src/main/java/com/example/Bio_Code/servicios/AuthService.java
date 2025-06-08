package com.example.Bio_Code.servicios;

import com.example.Bio_Code.modelo.Persona;
import com.example.Bio_Code.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Persona login(String correo, String contrasena) {
        Optional<Persona> personaOpt = usuarioRepository.findByCorreo(correo);
        if (personaOpt.isPresent()) {
            Persona persona = personaOpt.get();
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
