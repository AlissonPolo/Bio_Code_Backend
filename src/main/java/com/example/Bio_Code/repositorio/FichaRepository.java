package com.example.Bio_Code.repositorio;

import com.example.Bio_Code.modelo.Ficha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface FichaRepository extends JpaRepository<Ficha, Long> {
    Optional<Ficha> findByCodigo(String codigo);
}




