package com.example.Bio_Code.controlador;

import com.example.Bio_Code.dto.*;
import com.example.Bio_Code.modelo.Dispositivo;
import com.example.Bio_Code.modelo.ParqueaderoVehiculo;
import com.example.Bio_Code.servicios.IDispositivoService;
import com.example.Bio_Code.servicios.DispositivoPdfService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dispositivos")
//@CrossOrigin(origins = "*")
public class DispositivosController {

    @Autowired
    private IDispositivoService dispositivoService;

    @Autowired
    private DispositivoPdfService dispositivoPdfService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DispositivoDTO>>> obtenerTodos() {
        try {
            List<Dispositivo> dispositivos = dispositivoService.listarTodos();
            List<DispositivoDTO> dispositivosDTO = dispositivos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Dispositivos obtenidos exitosamente", dispositivosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener los dispositivos", e.getMessage()));
        }
    }

    @GetMapping("/consultaDia")
    public ResponseEntity<ApiResponse<List<DispositivoDTO>>> obtenerDiario() {
        try {
            List<Dispositivo> dispositivos = dispositivoService.listarDia();
            List<DispositivoDTO> dispositivosDTO = dispositivos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Dispositivos obtenidos exitosamente", dispositivosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener los dispositivos", e.getMessage()));
        }
    }


    @GetMapping("/numero-serie/{numeroSerie}")
    public ResponseEntity<ApiResponse<DispositivoDTO>> obtenerPorNumeroSerie(@PathVariable String numeroSerie) {
        try {
            Optional<Dispositivo> dispositivo = dispositivoService.obtenerPorNumeroSerie(numeroSerie);
            
            if (dispositivo.isPresent()) {
                DispositivoDTO dispositivoDTO = convertirADTO(dispositivo.get());
                return ResponseEntity.ok(ApiResponse.success("Dispositivo encontrado", dispositivoDTO));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Dispositivo no encontrado con número de serie: " + numeroSerie));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al buscar el dispositivo", e.getMessage()));
        }
    }

    @GetMapping("/codigo-inventario/{codigoInventario}")
    public ResponseEntity<ApiResponse<DispositivoDTO>> obtenerPorCodigoInventario(@PathVariable String codigoInventario) {
        try {
            Optional<Dispositivo> dispositivo = dispositivoService.obtenerPorCodigoInventario(codigoInventario);
            
            if (dispositivo.isPresent()) {
                DispositivoDTO dispositivoDTO = convertirADTO(dispositivo.get());
                return ResponseEntity.ok(ApiResponse.success("Dispositivo encontrado", dispositivoDTO));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Dispositivo no encontrado con código de inventario: " + codigoInventario));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al buscar el dispositivo", e.getMessage()));
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<DispositivoDTO>>> obtenerDispositivosActivos() {
        try {
            List<Dispositivo> dispositivos = dispositivoService.obtenerDispositivosActivos();
            List<DispositivoDTO> dispositivosDTO = dispositivos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Dispositivos activos obtenidos exitosamente", dispositivosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener dispositivos activos", e.getMessage()));
        }
    }

    @GetMapping("/requieren-mantenimiento")
    public ResponseEntity<ApiResponse<List<DispositivoDTO>>> obtenerDispositivosQueRequierenMantenimiento() {
        try {
            List<Dispositivo> dispositivos = dispositivoService.obtenerDispositivosQueRequierenMantenimiento();
            List<DispositivoDTO> dispositivosDTO = dispositivos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Dispositivos que requieren mantenimiento obtenidos exitosamente", dispositivosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener dispositivos que requieren mantenimiento", e.getMessage()));
        }
    }

    @GetMapping("/en-mantenimiento")
    public ResponseEntity<ApiResponse<List<DispositivoDTO>>> obtenerDispositivosEnMantenimiento() {
        try {
            List<Dispositivo> dispositivos = dispositivoService.obtenerDispositivosEnMantenimiento();
            List<DispositivoDTO> dispositivosDTO = dispositivos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Dispositivos en mantenimiento obtenidos exitosamente", dispositivosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener dispositivos en mantenimiento", e.getMessage()));
        }
    }

    @GetMapping("/danados")
    public ResponseEntity<ApiResponse<List<DispositivoDTO>>> obtenerDispositivosDañados() {
        try {
            List<Dispositivo> dispositivos = dispositivoService.obtenerDispositivosDañados();
            List<DispositivoDTO> dispositivosDTO = dispositivos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Dispositivos dañados obtenidos exitosamente", dispositivosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener dispositivos dañados", e.getMessage()));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<DispositivoDTO>>> buscarConFiltrosGet(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Dispositivo.TipoDispositivo tipo,
            @RequestParam(required = false) Dispositivo.EstadoDispositivo estado,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String responsable) {
        try {
            List<Dispositivo> dispositivos = dispositivoService.buscarConFiltros(
                nombre, tipo, estado, ubicacion, responsable
            );
            
            List<DispositivoDTO> dispositivosDTO = dispositivos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Búsqueda completada exitosamente", dispositivosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al buscar dispositivos", e.getMessage()));
        }
    }

    @PostMapping("/buscar")
    public ResponseEntity<ApiResponse<List<DispositivoDTO>>> buscarConFiltros(@RequestBody DispositivoFiltroDTO filtros) {
        try {
            List<Dispositivo> dispositivos = dispositivoService.buscarConFiltros(
                filtros.getNombre(),
                filtros.getTipo(),
                filtros.getEstado(),
                filtros.getUbicacion(),
                filtros.getResponsable()
            );
            
            List<DispositivoDTO> dispositivosDTO = dispositivos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Búsqueda completada exitosamente", dispositivosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al buscar dispositivos", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DispositivoDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            Optional<Dispositivo> dispositivo = dispositivoService.obtenerPorId(id);
            
            if (dispositivo.isPresent()) {
                DispositivoDTO dispositivoDTO = convertirADTO(dispositivo.get());
                return ResponseEntity.ok(ApiResponse.success("Dispositivo encontrado", dispositivoDTO));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Dispositivo no encontrado con ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener el dispositivo", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/ingreso_dispositivo")
    public ResponseEntity<ApiResponse<DispositivoDTO>>ingresoDispositivo(@PathVariable Long id){
        try {
            Dispositivo dispositivo = dispositivoService.marcarIngreso(id);
            DispositivoDTO dispositivoDTO = convertirADTO(dispositivo);
            return ResponseEntity.ok(ApiResponse.success("Ingreso registrado exitosamente", dispositivoDTO));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Datos invalidos", e.getMessage()));
        }catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("conflicto", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Error al marcar ingreso",e.getMessage()));
        }
    }

    @PatchMapping("/{id}/salida")
    public ResponseEntity<ApiResponse<DispositivoDTO>> marcarSalida(@PathVariable Long id) {
        try {
            Dispositivo dispositivo = dispositivoService.marcarSalida(id);
            DispositivoDTO dispositivoDTO = convertirADTO(dispositivo);

            return ResponseEntity.ok(ApiResponse.success("Salida registrada exitosamente", dispositivoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Conflicto", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al marcar la salida", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DispositivoDTO>> crear(@Valid @RequestBody DispositivoDTO dispositivoDTO) {
        try {
            Dispositivo dispositivo = convertirAEntidad(dispositivoDTO);
            Dispositivo dispositivoCreado = dispositivoService.crear(dispositivo);
            DispositivoDTO dispositivoCreadoDTO = convertirADTO(dispositivoCreado);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Dispositivo creado exitosamente", dispositivoCreadoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Conflicto", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al crear el dispositivo", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DispositivoDTO>> actualizar(@PathVariable Long id, 
            @RequestBody DispositivoDTO dispositivoDTO) {
        try {
            Dispositivo dispositivo = convertirAEntidad(dispositivoDTO);
            Dispositivo dispositivoActualizado = dispositivoService.actualizar(id, dispositivo);
            DispositivoDTO dispositivoActualizadoDTO = convertirADTO(dispositivoActualizado);
            
            return ResponseEntity.ok(ApiResponse.success("Dispositivo actualizado exitosamente", dispositivoActualizadoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Conflicto", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al actualizar el dispositivo", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/mantenimiento/iniciar")
    public ResponseEntity<ApiResponse<DispositivoDTO>> marcarEnMantenimiento(@PathVariable Long id, 
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String observaciones = body != null ? body.get("observaciones") : null;
            Dispositivo dispositivo = dispositivoService.marcarEnMantenimiento(id, observaciones);
            DispositivoDTO dispositivoDTO = convertirADTO(dispositivo);
            
            return ResponseEntity.ok(ApiResponse.success("Dispositivo marcado en mantenimiento", dispositivoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al marcar dispositivo en mantenimiento", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/mantenimiento/completar")
    public ResponseEntity<ApiResponse<DispositivoDTO>> completarMantenimiento(@PathVariable Long id, 
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String observaciones = body != null ? body.get("observaciones") : null;
            Dispositivo dispositivo = dispositivoService.completarMantenimiento(id, observaciones);
            DispositivoDTO dispositivoDTO = convertirADTO(dispositivo);
            
            return ResponseEntity.ok(ApiResponse.success("Mantenimiento completado exitosamente", dispositivoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al completar mantenimiento", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<DispositivoDTO>> cambiarEstado(@PathVariable Long id, 
            @RequestBody Map<String, Object> body) {
        try {
            String estadoStr = (String) body.get("estado");
            String observaciones = (String) body.get("observaciones");
            
            if (estadoStr == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("El estado es obligatorio"));
            }
            
            Dispositivo.EstadoDispositivo nuevoEstado = Dispositivo.EstadoDispositivo.valueOf(estadoStr);
            Dispositivo dispositivo = dispositivoService.cambiarEstado(id, nuevoEstado, observaciones);
            DispositivoDTO dispositivoDTO = convertirADTO(dispositivo);
            
            return ResponseEntity.ok(ApiResponse.success("Estado cambiado exitosamente", dispositivoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al cambiar estado", e.getMessage()));
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<ApiResponse<EstadisticasDispositivosDTO>> obtenerEstadisticas() {
        try {
            EstadisticasDispositivosDTO estadisticas = new EstadisticasDispositivosDTO();
            
            Map<Dispositivo.TipoDispositivo, Long> porTipo = dispositivoService.obtenerEstadisticasPorTipo();
            Map<String, Long> porTipoStr = new HashMap<>();
            porTipo.forEach((tipo, count) -> porTipoStr.put(tipo.getDisplayName(), count));
            estadisticas.setDispositivosPorTipo(porTipoStr);
            
            Map<Dispositivo.EstadoDispositivo, Long> porEstado = dispositivoService.obtenerEstadisticasPorEstado();
            Map<String, Long> porEstadoStr = new HashMap<>();
            porEstado.forEach((estado, count) -> porEstadoStr.put(estado.getDisplayName(), count));
            estadisticas.setDispositivosPorEstado(porEstadoStr);
            
            estadisticas.setTotalDispositivos(dispositivoService.listarTodos().size());
            estadisticas.setDispositivosActivos(dispositivoService.obtenerDispositivosActivos().size());
            estadisticas.setDispositivosEnMantenimiento(dispositivoService.obtenerDispositivosEnMantenimiento().size());
            estadisticas.setDispositivosQueRequierenMantenimiento(dispositivoService.obtenerDispositivosQueRequierenMantenimiento().size());
            estadisticas.setDispositivosDañados(dispositivoService.obtenerDispositivosDañados().size());
            
            return ResponseEntity.ok(ApiResponse.success("Estadísticas obtenidas exitosamente", estadisticas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener estadísticas", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        try {
            dispositivoService.eliminar(id);
            return ResponseEntity.ok(ApiResponse.success("Dispositivo eliminado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al eliminar el dispositivo", e.getMessage()));
        }
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generarReportePdf(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Dispositivo.TipoDispositivo tipo,
            @RequestParam(required = false) Dispositivo.EstadoDispositivo estado,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String responsable) {
        try {
            List<Dispositivo> dispositivos;
            boolean esFiltrado = false;
            
            // Verificar si se aplicaron filtros
            if (nombre != null || tipo != null || estado != null || ubicacion != null || responsable != null) {
                // Aplicar filtros usando la misma lógica que el endpoint de búsqueda
                dispositivos = dispositivoService.buscarConFiltros(nombre, tipo, estado, ubicacion, responsable);
                esFiltrado = true;
            } else {
                // Sin filtros, obtener todos los dispositivos
                dispositivos = dispositivoService.listarTodos();
            }
            
            // Validar que se encontraron dispositivos
            if (dispositivos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            
            // Crear objeto con información de filtros para el PDF
            DispositivoFiltroDTO filtrosAplicados = null;
            if (esFiltrado) {
                filtrosAplicados = new DispositivoFiltroDTO();
                filtrosAplicados.setNombre(nombre);
                filtrosAplicados.setTipo(tipo);
                filtrosAplicados.setEstado(estado);
                filtrosAplicados.setUbicacion(ubicacion);
                filtrosAplicados.setResponsable(responsable);
            }
            
            // Generar el PDF con información de filtros
            byte[] pdfBytes = dispositivoPdfService.generarReporteDispositivos(dispositivos, filtrosAplicados);
            
            // Generar nombre de archivo dinámico
            String nombreArchivo = generarNombreArchivo(esFiltrado);
            
            // Configurar headers para descarga del archivo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", nombreArchivo);
            headers.setContentLength(pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (Exception e) {
            // En caso de error, retornamos un response con status 500
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            String errorJson = "{\"success\":false,\"message\":\"Error al generar el reporte PDF\",\"error\":\"" + e.getMessage() + "\"}";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .body(errorJson.getBytes());
        }
    }

    private DispositivoDTO convertirADTO(Dispositivo dispositivo) {
        DispositivoDTO dto = new DispositivoDTO();
        dto.setId(dispositivo.getId());
        dto.setNombre(dispositivo.getNombre());
        dto.setTipo(dispositivo.getTipo());
        dto.setMarca(dispositivo.getMarca());
        dto.setModelo(dispositivo.getModelo());
        dto.setNumeroSerie(dispositivo.getNumeroSerie());
        dto.setCodigoInventario(dispositivo.getCodigoInventario());
        dto.setEstado(dispositivo.getEstado());
        dto.setUbicacion(dispositivo.getUbicacion());
        dto.setResponsable(dispositivo.getResponsable());
        dto.setFechaAdquisicion(dispositivo.getFechaAdquisicion());
        dto.setFechaUltimoMantenimiento(dispositivo.getFechaUltimoMantenimiento());
        dto.setObservaciones(dispositivo.getObservaciones());
        dto.setCreadoEn(dispositivo.getCreadoEn());
        dto.setActualizadoEn(dispositivo.getActualizadoEn());
        dto.setRequiereMantenimiento(dispositivo.requiereMantenimiento());
        return dto;
    }

    private Dispositivo convertirAEntidad(DispositivoDTO dto) {
        Dispositivo dispositivo = new Dispositivo();
        dispositivo.setNombre(dto.getNombre());
        dispositivo.setTipo(dto.getTipo());
        dispositivo.setMarca(dto.getMarca());
        dispositivo.setModelo(dto.getModelo());
        dispositivo.setNumeroSerie(dto.getNumeroSerie());
        dispositivo.setCodigoInventario(dto.getCodigoInventario());
        dispositivo.setEstado(dto.getEstado());
        dispositivo.setUbicacion(dto.getUbicacion());
        dispositivo.setResponsable(dto.getResponsable());
        dispositivo.setFechaAdquisicion(dto.getFechaAdquisicion());
        dispositivo.setFechaUltimoMantenimiento(dto.getFechaUltimoMantenimiento());
        dispositivo.setObservaciones(dto.getObservaciones());
        return dispositivo;
    }
    
    /**
     * Genera el nombre del archivo PDF según si se aplicaron filtros o no
     */
    private String generarNombreArchivo(boolean esFiltrado) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = dateFormat.format(new java.util.Date());
        
        if (esFiltrado) {
            return "reporte-dispositivos-filtrado-" + fecha + ".pdf";
        } else {
            return "reporte-dispositivos-completo-" + fecha + ".pdf";
        }
    }
}
