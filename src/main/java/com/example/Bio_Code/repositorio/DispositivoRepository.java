package com.example.Bio_Code.repositorio;

import com.example.Bio_Code.modelo.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
    
    // Buscar por número de serie
    Optional<Dispositivo> findByNumeroSerie(String numeroSerie);
    
    // Buscar por código de inventario
    Optional<Dispositivo> findByCodigoInventario(String codigoInventario);
    
    // Buscar por tipo de dispositivo
    List<Dispositivo> findByTipo(Dispositivo.TipoDispositivo tipo);
    
    // Buscar por estado
    List<Dispositivo> findByEstado(Dispositivo.EstadoDispositivo estado);
    
    // Buscar por ubicación
    List<Dispositivo> findByUbicacionContainingIgnoreCase(String ubicacion);
    
    // Buscar por responsable
    List<Dispositivo> findByResponsableContainingIgnoreCase(String responsable);
    
    // Buscar por nombre
    List<Dispositivo> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por marca
    List<Dispositivo> findByMarcaContainingIgnoreCase(String marca);
    
    // Dispositivos que requieren mantenimiento (más de 6 meses)
    @Query("SELECT d FROM Dispositivo d WHERE d.fechaUltimoMantenimiento IS NULL OR d.fechaUltimoMantenimiento < :fechaLimite")
    List<Dispositivo> findDispositivosQueRequierenMantenimiento(@Param("fechaLimite") Instant fechaLimite);
    
    // Dispositivos activos
    @Query("SELECT d FROM Dispositivo d WHERE d.estado = 'ACTIVO'")
    List<Dispositivo> findDispositivosActivos();
    
    // Dispositivos en mantenimiento
    @Query("SELECT d FROM Dispositivo d WHERE d.estado = 'EN_MANTENIMIENTO'")
    List<Dispositivo> findDispositivosEnMantenimiento();
    
    // Dispositivos dañados
    @Query("SELECT d FROM Dispositivo d WHERE d.estado = 'DAÑADO'")
    List<Dispositivo> findDispositivosDañados();
    
    // Contar dispositivos por tipo
    @Query("SELECT d.tipo, COUNT(d) FROM Dispositivo d GROUP BY d.tipo")
    List<Object[]> countDispositivosPorTipo();
    
    // Contar dispositivos por estado
    @Query("SELECT d.estado, COUNT(d) FROM Dispositivo d GROUP BY d.estado")
    List<Object[]> countDispositivosPorEstado();
    
    // Verificar si existe un dispositivo con el número de serie
    boolean existsByNumeroSerie(String numeroSerie);
    
    // Verificar si existe un dispositivo con el código de inventario
    boolean existsByCodigoInventario(String codigoInventario);
    
    // Buscar dispositivos por múltiples criterios
    @Query(value = "SELECT * FROM dispositivos d WHERE " +
           "(:nombre IS NULL OR " +
           "  UPPER(d.nombre) LIKE UPPER(CONCAT('%', :nombre, '%')) OR " +
           "  UPPER(d.numero_serie) LIKE UPPER(CONCAT('%', :nombre, '%')) OR " +
           "  UPPER(d.codigo_inventario) LIKE UPPER(CONCAT('%', :nombre, '%'))) AND " +
           "(:tipo IS NULL OR d.tipo = CAST(:tipo AS VARCHAR)) AND " +
           "(:estado IS NULL OR d.estado = CAST(:estado AS VARCHAR)) AND " +
           "(:ubicacion IS NULL OR UPPER(d.ubicacion) LIKE UPPER(CONCAT('%', :ubicacion, '%'))) AND " +
           "(:responsable IS NULL OR UPPER(d.responsable) LIKE UPPER(CONCAT('%', :responsable, '%')))",
           nativeQuery = true)
    List<Dispositivo> findDispositivosConFiltros(
        @Param("nombre") String nombre,
        @Param("tipo") String tipo,
        @Param("estado") String estado,
        @Param("ubicacion") String ubicacion,
        @Param("responsable") String responsable
    );

    @Query("SELECT d FROM Dispositivo d " +
            "WHERE d.actualizadoEn BETWEEN :inicio AND :fin " +
            "AND d.fechaAdquisicion IS NULL " +
            "AND d.actualizadoEn <> d.creadoEn")
    List<Dispositivo> listarDia(
            @Param("inicio") Instant inicio,
            @Param("fin") Instant fin
    );

}


