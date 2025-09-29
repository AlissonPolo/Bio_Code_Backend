package com.example.Bio_Code.servicios.impl;

import com.example.Bio_Code.modelo.Dispositivo;
import com.example.Bio_Code.repositorio.DispositivoRepository;
import com.example.Bio_Code.servicios.IDispositivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DispositivoService implements IDispositivoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Override
    public Dispositivo crear(Dispositivo dispositivo) {
        if (dispositivo == null) {
            throw new IllegalArgumentException("El dispositivo no puede ser nulo");
        }
        
        if (dispositivo.getNombre() == null || dispositivo.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del dispositivo es obligatorio");
        }
        
        if (dispositivo.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de dispositivo es obligatorio");
        }
        
        if (dispositivo.getEstado() == null) {
            throw new IllegalArgumentException("El estado del dispositivo es obligatorio");
        }
        
        // Verificar unicidad de número de serie
        if (dispositivo.getNumeroSerie() != null && 
            dispositivoRepository.existsByNumeroSerie(dispositivo.getNumeroSerie())) {
            throw new IllegalStateException("Ya existe un dispositivo con el número de serie: " + 
                                          dispositivo.getNumeroSerie());
        }
        
        // Verificar unicidad de código de inventario
        if (dispositivo.getCodigoInventario() != null && 
            dispositivoRepository.existsByCodigoInventario(dispositivo.getCodigoInventario())) {
            throw new IllegalStateException("Ya existe un dispositivo con el código de inventario: " + 
                                          dispositivo.getCodigoInventario());
        }
        
        // Normalizar datos
        dispositivo.setNombre(dispositivo.getNombre().trim());
        if (dispositivo.getMarca() != null) {
            dispositivo.setMarca(dispositivo.getMarca().trim());
        }
        if (dispositivo.getModelo() != null) {
            dispositivo.setModelo(dispositivo.getModelo().trim());
        }
        if (dispositivo.getUbicacion() != null) {
            dispositivo.setUbicacion(dispositivo.getUbicacion().trim());
        }
        if (dispositivo.getResponsable() != null) {
            dispositivo.setResponsable(dispositivo.getResponsable().trim());
        }
        
        return dispositivoRepository.save(dispositivo);
    }

    @Override
    public Optional<Dispositivo> obtenerPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return dispositivoRepository.findById(id);
    }

    @Override
    public List<Dispositivo> listarTodos() {
        return dispositivoRepository.findAll();
    }

    @Override
    public Dispositivo actualizar(Long id, Dispositivo dispositivo) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        
        Optional<Dispositivo> dispositivoExistente = dispositivoRepository.findById(id);
        if (dispositivoExistente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el dispositivo con ID: " + id);
        }
        
        Dispositivo dispositivoAActualizar = dispositivoExistente.get();
        
        // Actualizar campos si vienen en el request
        if (dispositivo.getNombre() != null && !dispositivo.getNombre().trim().isEmpty()) {
            dispositivoAActualizar.setNombre(dispositivo.getNombre().trim());
        }
        
        if (dispositivo.getTipo() != null) {
            dispositivoAActualizar.setTipo(dispositivo.getTipo());
        }
        
        if (dispositivo.getMarca() != null) {
            dispositivoAActualizar.setMarca(dispositivo.getMarca().trim());
        }
        
        if (dispositivo.getModelo() != null) {
            dispositivoAActualizar.setModelo(dispositivo.getModelo().trim());
        }
        
        if (dispositivo.getNumeroSerie() != null) {
            // Verificar que no existe otro dispositivo con el mismo número de serie
            Optional<Dispositivo> existeConSerie = dispositivoRepository.findByNumeroSerie(dispositivo.getNumeroSerie());
            if (existeConSerie.isPresent() && !existeConSerie.get().getId().equals(id)) {
                throw new IllegalStateException("Ya existe otro dispositivo con el número de serie: " + 
                                              dispositivo.getNumeroSerie());
            }
            dispositivoAActualizar.setNumeroSerie(dispositivo.getNumeroSerie());
        }
        
        if (dispositivo.getCodigoInventario() != null) {
            // Verificar que no existe otro dispositivo con el mismo código
            Optional<Dispositivo> existeConCodigo = dispositivoRepository.findByCodigoInventario(dispositivo.getCodigoInventario());
            if (existeConCodigo.isPresent() && !existeConCodigo.get().getId().equals(id)) {
                throw new IllegalStateException("Ya existe otro dispositivo con el código de inventario: " + 
                                              dispositivo.getCodigoInventario());
            }
            dispositivoAActualizar.setCodigoInventario(dispositivo.getCodigoInventario());
        }
        
        if (dispositivo.getEstado() != null) {
            dispositivoAActualizar.setEstado(dispositivo.getEstado());
        }
        
        if (dispositivo.getUbicacion() != null) {
            dispositivoAActualizar.setUbicacion(dispositivo.getUbicacion().trim());
        }
        
        if (dispositivo.getResponsable() != null) {
            dispositivoAActualizar.setResponsable(dispositivo.getResponsable().trim());
        }
        
        if (dispositivo.getFechaAdquisicion() != null) {
            dispositivoAActualizar.setFechaAdquisicion(dispositivo.getFechaAdquisicion());
        }
        
        if (dispositivo.getFechaUltimoMantenimiento() != null) {
            dispositivoAActualizar.setFechaUltimoMantenimiento(dispositivo.getFechaUltimoMantenimiento());
        }
        
        if (dispositivo.getObservaciones() != null) {
            dispositivoAActualizar.setObservaciones(dispositivo.getObservaciones());
        }
        
        return dispositivoRepository.save(dispositivoAActualizar);
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        
        if (!dispositivoRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró el dispositivo con ID: " + id);
        }
        
        dispositivoRepository.deleteById(id);
    }

    @Override
    public Optional<Dispositivo> obtenerPorNumeroSerie(String numeroSerie) {
        if (numeroSerie == null || numeroSerie.trim().isEmpty()) {
            return Optional.empty();
        }
        return dispositivoRepository.findByNumeroSerie(numeroSerie.trim());
    }

    @Override
    public Optional<Dispositivo> obtenerPorCodigoInventario(String codigoInventario) {
        if (codigoInventario == null || codigoInventario.trim().isEmpty()) {
            return Optional.empty();
        }
        return dispositivoRepository.findByCodigoInventario(codigoInventario.trim());
    }

    @Override
    public List<Dispositivo> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }
        return dispositivoRepository.findByNombreContainingIgnoreCase(nombre.trim());
    }

    @Override
    public List<Dispositivo> buscarPorTipo(Dispositivo.TipoDispositivo tipo) {
        if (tipo == null) {
            return List.of();
        }
        return dispositivoRepository.findByTipo(tipo);
    }

    @Override
    public List<Dispositivo> buscarPorEstado(Dispositivo.EstadoDispositivo estado) {
        if (estado == null) {
            return List.of();
        }
        return dispositivoRepository.findByEstado(estado);
    }

    @Override
    public List<Dispositivo> buscarPorUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            return List.of();
        }
        return dispositivoRepository.findByUbicacionContainingIgnoreCase(ubicacion.trim());
    }

    @Override
    public List<Dispositivo> buscarPorResponsable(String responsable) {
        if (responsable == null || responsable.trim().isEmpty()) {
            return List.of();
        }
        return dispositivoRepository.findByResponsableContainingIgnoreCase(responsable.trim());
    }

    @Override
    public List<Dispositivo> buscarPorMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            return List.of();
        }
        return dispositivoRepository.findByMarcaContainingIgnoreCase(marca.trim());
    }

    @Override
    public List<Dispositivo> obtenerDispositivosActivos() {
        return dispositivoRepository.findDispositivosActivos();
    }

    @Override
    public List<Dispositivo> obtenerDispositivosQueRequierenMantenimiento() {
        // Dispositivos que no han recibido mantenimiento en 6 meses
        Instant fechaLimite = Instant.now().minusSeconds(6 * 30 * 24 * 60 * 60L);
        return dispositivoRepository.findDispositivosQueRequierenMantenimiento(fechaLimite);
    }

    @Override
    public List<Dispositivo> obtenerDispositivosEnMantenimiento() {
        return dispositivoRepository.findDispositivosEnMantenimiento();
    }

    @Override
    public List<Dispositivo> obtenerDispositivosDañados() {
        return dispositivoRepository.findDispositivosDañados();
    }

    @Override
    public Dispositivo marcarEnMantenimiento(Long id, String observaciones) {
        Optional<Dispositivo> dispositivoOpt = dispositivoRepository.findById(id);
        if (dispositivoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el dispositivo con ID: " + id);
        }
        
        Dispositivo dispositivo = dispositivoOpt.get();
        dispositivo.setEstado(Dispositivo.EstadoDispositivo.EN_MANTENIMIENTO);
        
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            String nuevasObservaciones = (dispositivo.getObservaciones() != null ? 
                dispositivo.getObservaciones() + "\n" : "") + 
                "[" + Instant.now() + "] Mantenimiento iniciado: " + observaciones.trim();
            dispositivo.setObservaciones(nuevasObservaciones);
        }
        
        return dispositivoRepository.save(dispositivo);
    }

    @Override
    public Dispositivo completarMantenimiento(Long id, String observaciones) {
        Optional<Dispositivo> dispositivoOpt = dispositivoRepository.findById(id);
        if (dispositivoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el dispositivo con ID: " + id);
        }
        
        Dispositivo dispositivo = dispositivoOpt.get();
        dispositivo.setEstado(Dispositivo.EstadoDispositivo.ACTIVO);
        dispositivo.setFechaUltimoMantenimiento(Instant.now());
        
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            String nuevasObservaciones = (dispositivo.getObservaciones() != null ? 
                dispositivo.getObservaciones() + "\n" : "") + 
                "[" + Instant.now() + "] Mantenimiento completado: " + observaciones.trim();
            dispositivo.setObservaciones(nuevasObservaciones);
        }
        
        return dispositivoRepository.save(dispositivo);
    }

    @Override
    public Dispositivo cambiarEstado(Long id, Dispositivo.EstadoDispositivo nuevoEstado, String observaciones) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }
        
        Optional<Dispositivo> dispositivoOpt = dispositivoRepository.findById(id);
        if (dispositivoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el dispositivo con ID: " + id);
        }
        
        Dispositivo dispositivo = dispositivoOpt.get();
        Dispositivo.EstadoDispositivo estadoAnterior = dispositivo.getEstado();
        dispositivo.setEstado(nuevoEstado);
        
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            String nuevasObservaciones = (dispositivo.getObservaciones() != null ? 
                dispositivo.getObservaciones() + "\n" : "") + 
                "[" + Instant.now() + "] Cambio de estado de " + estadoAnterior + " a " + nuevoEstado + ": " + observaciones.trim();
            dispositivo.setObservaciones(nuevasObservaciones);
        }
        
        return dispositivoRepository.save(dispositivo);
    }

    @Override
    public Map<Dispositivo.TipoDispositivo, Long> obtenerEstadisticasPorTipo() {
        List<Object[]> resultados = dispositivoRepository.countDispositivosPorTipo();
        Map<Dispositivo.TipoDispositivo, Long> estadisticas = new HashMap<>();
        
        for (Object[] resultado : resultados) {
            Dispositivo.TipoDispositivo tipo = (Dispositivo.TipoDispositivo) resultado[0];
            Long count = (Long) resultado[1];
            estadisticas.put(tipo, count);
        }
        
        return estadisticas;
    }

    @Override
    public Map<Dispositivo.EstadoDispositivo, Long> obtenerEstadisticasPorEstado() {
        List<Object[]> resultados = dispositivoRepository.countDispositivosPorEstado();
        Map<Dispositivo.EstadoDispositivo, Long> estadisticas = new HashMap<>();
        
        for (Object[] resultado : resultados) {
            Dispositivo.EstadoDispositivo estado = (Dispositivo.EstadoDispositivo) resultado[0];
            Long count = (Long) resultado[1];
            estadisticas.put(estado, count);
        }
        
        return estadisticas;
    }

    @Override
    public List<Dispositivo> buscarConFiltros(String nombre, Dispositivo.TipoDispositivo tipo, 
                                            Dispositivo.EstadoDispositivo estado, String ubicacion, 
                                            String responsable) {
        return dispositivoRepository.findDispositivosConFiltros(
            nombre != null && !nombre.trim().isEmpty() ? nombre.trim() : null,
            tipo,
            estado,
            ubicacion != null && !ubicacion.trim().isEmpty() ? ubicacion.trim() : null,
            responsable != null && !responsable.trim().isEmpty() ? responsable.trim() : null
        );
    }
}

