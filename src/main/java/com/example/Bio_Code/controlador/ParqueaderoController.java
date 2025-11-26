package com.example.Bio_Code.controlador;

import com.example.Bio_Code.dto.ApiResponse;
import com.example.Bio_Code.dto.ParqueaderoVehiculoDTO;
import com.example.Bio_Code.modelo.ParqueaderoVehiculo;
import com.example.Bio_Code.servicios.EmailService;
import com.example.Bio_Code.servicios.IParqueaderoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parqueadero")
//@CrossOrigin(origins = "")
public class ParqueaderoController {

    @Autowired
    private IParqueaderoService parqueaderoService;

    @Autowired
    private EmailService emailService;


    @GetMapping("/hoy")
    public ResponseEntity<ApiResponse<List<ParqueaderoVehiculoDTO>>> obtenerPorFechaHoy() {
        try {
            List<ParqueaderoVehiculo> vehiculos = parqueaderoService.listarPorFechaHoy();

            List<ParqueaderoVehiculoDTO> vehiculosDTO = vehiculos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(
                    ApiResponse.success("Vehículos del día obtenidos exitosamente", vehiculosDTO)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener los vehículos del día", e.getMessage()));
        }
    }



    @GetMapping("/parqueados")
    public ResponseEntity<ApiResponse<List<ParqueaderoVehiculoDTO>>> obtenerVehiculosParqueados() {
        try {
            List<ParqueaderoVehiculo> vehiculos = parqueaderoService.obtenerVehiculosParqueados();
            List<ParqueaderoVehiculoDTO> vehiculosDTO = vehiculos.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Vehículos parqueados obtenidos exitosamente", vehiculosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener los vehículos parqueados", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParqueaderoVehiculoDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            Optional<ParqueaderoVehiculo> vehiculo = parqueaderoService.obtenerPorId(id);
            
            if (vehiculo.isPresent()) {
                ParqueaderoVehiculoDTO vehiculoDTO = convertirADTO(vehiculo.get());
                return ResponseEntity.ok(ApiResponse.success("Vehículo encontrado", vehiculoDTO));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Vehículo no encontrado con ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener el vehículo", e.getMessage()));
        }
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<ApiResponse<ParqueaderoVehiculoDTO>> obtenerPorPlaca(@PathVariable String placa) {
        try {
            Optional<ParqueaderoVehiculo> vehiculo = parqueaderoService.obtenerPorPlaca(placa);
            
            if (vehiculo.isPresent()) {
                ParqueaderoVehiculoDTO vehiculoDTO = convertirADTO(vehiculo.get());
                return ResponseEntity.ok(ApiResponse.success("Vehículo encontrado", vehiculoDTO));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Vehículo no encontrado con placa: " + placa));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al buscar el vehículo", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ParqueaderoVehiculoDTO>> crear(@Valid @RequestBody ParqueaderoVehiculoDTO vehiculoDTO) {

        try {
            ParqueaderoVehiculo vehiculo = convertirAEntidad(vehiculoDTO);
            ParqueaderoVehiculo vehiculoCreado = parqueaderoService.crear(vehiculo);

            ParqueaderoVehiculoDTO vehiculoCreadoDTO = convertirADTO(vehiculoCreado);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Vehículo registrado exitosamente", vehiculoCreadoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Conflicto", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al crear el registro", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParqueaderoVehiculoDTO>> actualizar(@PathVariable Long id, 
            @RequestBody ParqueaderoVehiculoDTO vehiculoDTO) {
        try {
            ParqueaderoVehiculo vehiculo = convertirAEntidad(vehiculoDTO);
            ParqueaderoVehiculo vehiculoActualizado = parqueaderoService.actualizar(id, vehiculo);
            ParqueaderoVehiculoDTO vehiculoActualizadoDTO = convertirADTO(vehiculoActualizado);
            
            return ResponseEntity.ok(ApiResponse.success("Vehículo actualizado exitosamente", vehiculoActualizadoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al actualizar el vehículo", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/ingreso")
    public ResponseEntity<ApiResponse<ParqueaderoVehiculoDTO>> marcarIngreso(@PathVariable Long id) {
        try {
            // Llamamos al servicio para registrar la entrada
            ParqueaderoVehiculo vehiculo = parqueaderoService.marcarIngreso(id);
            ParqueaderoVehiculoDTO vehiculoDTO = convertirADTO(vehiculo);

            return ResponseEntity.ok(ApiResponse.success("Ingreso registrado exitosamente", vehiculoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Conflicto", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al marcar el ingreso", e.getMessage()));
        }
    }



    @PatchMapping("/{id}/salida")
    public ResponseEntity<ApiResponse<ParqueaderoVehiculoDTO>> marcarSalida(@PathVariable Long id) {
        try {
            ParqueaderoVehiculo vehiculo = parqueaderoService.marcarSalida(id);
            ParqueaderoVehiculoDTO vehiculoDTO = convertirADTO(vehiculo);
            
            return ResponseEntity.ok(ApiResponse.success("Salida registrada exitosamente", vehiculoDTO));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        try {
            parqueaderoService.eliminar(id);
            return ResponseEntity.ok(ApiResponse.success("Vehículo eliminado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al eliminar el vehículo", e.getMessage()));
        }
    }

    private ParqueaderoVehiculoDTO convertirADTO(ParqueaderoVehiculo vehiculo) {
        ParqueaderoVehiculoDTO dto = new ParqueaderoVehiculoDTO();
        dto.setId(vehiculo.getId());
        dto.setPlaca(vehiculo.getPlaca());
        dto.setTipo(vehiculo.getTipo());
        dto.setMarca(vehiculo.getMarca());
        dto.setModelo(vehiculo.getModelo());
        dto.setColor(vehiculo.getColor());
        dto.setFechaEntrada(vehiculo.getFechaEntrada());
        dto.setFechaSalida(vehiculo.getFechaSalida());
        dto.setCreadoEn(vehiculo.getCreadoEn());
        dto.setActualizadoEn(vehiculo.getActualizadoEn());
        dto.setCorreoElectronico(vehiculo.getCorreoElectronico());
        return dto;
    }

    private ParqueaderoVehiculo convertirAEntidad(ParqueaderoVehiculoDTO dto) {

        System.out.println("📥 [LOG 1] DTO recibido del FRONT:");
        System.out.println("Placa: " + dto.getPlaca());
        System.out.println("Correo recibido: " + dto.getCorreoElectronico());

        ParqueaderoVehiculo vehiculo = new ParqueaderoVehiculo();
        vehiculo.setId(dto.getId());
        vehiculo.setPlaca(dto.getPlaca());
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setColor(dto.getColor());
        vehiculo.setFechaEntrada(dto.getFechaEntrada());
        vehiculo.setFechaSalida(dto.getFechaSalida());
        vehiculo.setCorreoElectronico(dto.getCorreoElectronico());

        return vehiculo;
    }
}
