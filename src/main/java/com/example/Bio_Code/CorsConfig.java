package com.example.Bio_Code;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // aplica a todas las rutas
                        .allowedOrigins(
                                "http://localhost:4200", // Angular local
                                "https://biocode-production.up.railway.app" // frontend en producción
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // incluye OPTIONS para preflight
                        .allowedHeaders("*") // permite todos los headers
                        .allowCredentials(true); // necesario si usas cookies
            }
        };
    }
}
