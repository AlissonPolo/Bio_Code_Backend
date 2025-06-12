package com.example.Bio_Code.controlador;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "¡Bienvenido a BIO_CODE!";
    }
}
