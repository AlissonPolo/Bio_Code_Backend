package com.example.Bio_Code.servicios;

import com.example.Bio_Code.modelo.Dispositivo;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IDispositivoService {
    
    // CRUD básico
    Dispositivo crear(Dispositivo dispositivo);
    Optional<Dispositivo> obtenerPorId(Long id);
    List<Dispositivo> listarTodos();
    Dispositivo actualizar(Long id, Dispositivo dispositivo);
    void eliminar(Long id);
    
    // Búsquedas específicas
    Optional<Dispositivo> obtenerPorNumeroSerie(String numeroSerie);
    Optional<Dispositivo> obtenerPorCodigoInventario(String codigoInventario);
    List<Dispositivo> buscarPorNombre(String nombre);
    List<Dispositivo> buscarPorTipo(Dispositivo.TipoDispositivo tipo);
    List<Dispositivo> buscarPorEstado(Dispositivo.EstadoDispositivo estado);
    List<Dispositivo> buscarPorUbicacion(String ubicacion);
    List<Dispositivo> buscarPorResponsable(String responsable);
    List<Dispositivo> buscarPorMarca(String marca);
    
    // Operaciones especiales
    List<Dispositivo> obtenerDispositivosActivos();
    List<Dispositivo> obtenerDispositivosQueRequierenMantenimiento();
    List<Dispositivo> obtenerDispositivosEnMantenimiento();
    List<Dispositivo> obtenerDispositivosDañados();
    
    // Mantenimiento
    Dispositivo marcarEnMantenimiento(Long id, String observaciones);
    Dispositivo completarMantenimiento(Long id, String observaciones);
    Dispositivo cambiarEstado(Long id, Dispositivo.EstadoDispositivo nuevoEstado, String observaciones);
    
    // Reportes y estadísticas
    Map<Dispositivo.TipoDispositivo, Long> obtenerEstadisticasPorTipo();
    Map<Dispositivo.EstadoDispositivo, Long> obtenerEstadisticasPorEstado();
    
    // Búsqueda avanzada
    List<Dispositivo> buscarConFiltros(String nombre, Dispositivo.TipoDispositivo tipo, 
                                      Dispositivo.EstadoDispositivo estado, String ubicacion, 
                                      String responsable);
    //registro diario
    List<Dispositivo> listarDia();

}




