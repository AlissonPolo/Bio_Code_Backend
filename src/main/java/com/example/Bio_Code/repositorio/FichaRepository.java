package com.example.Bio_Code.repositorio;

import com.example.Bio_Code.modelo.Ficha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FichaRepository extends JpaRepository<Ficha, Long> {

}




