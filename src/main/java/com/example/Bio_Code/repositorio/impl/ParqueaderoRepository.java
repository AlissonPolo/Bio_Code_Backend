package com.example.Bio_Code.repositorio.impl;

import com.example.Bio_Code.modelo.ParqueaderoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParqueaderoRepository extends JpaRepository<ParqueaderoVehiculo, Long> {
    
    // Buscar vehículo por placa (case insensitive)
    @Query("SELECT p FROM ParqueaderoVehiculo p WHERE UPPER(p.placa) = UPPER(:placa)")
    Optional<ParqueaderoVehiculo> findByPlacaIgnoreCase(@Param("placa") String placa);
    
    // Buscar vehículos actualmente parqueados (sin fecha de salida)
    @Query("SELECT p FROM ParqueaderoVehiculo p WHERE p.fechaSalida IS NULL")
    List<ParqueaderoVehiculo> findVehiculosActualmenteParqueados();
    
    // Verificar si existe un vehículo parqueado con esa placa
    @Query("SELECT COUNT(p) > 0 FROM ParqueaderoVehiculo p WHERE UPPER(p.placa) = UPPER(:placa) AND p.fechaSalida IS NULL")
    boolean existeVehiculoParqueadoConPlaca(@Param("placa") String placa);
}
