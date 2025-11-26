package com.example.Bio_Code.servicios.impl;

import com.example.Bio_Code.modelo.ParqueaderoVehiculo;
import com.example.Bio_Code.repositorio.impl.ParqueaderoRepository;
import com.example.Bio_Code.servicios.EmailService;
import com.example.Bio_Code.servicios.IParqueaderoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ParqueaderoService implements IParqueaderoService {

    @Autowired
    private ParqueaderoRepository parqueaderoRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public ParqueaderoVehiculo crear(ParqueaderoVehiculo vehiculo) {
        System.out.println("📤 [LOG 2] Vehículo recibido en SERVICE antes de guardar:");
        System.out.println("Placa: " + vehiculo.getPlaca());
        System.out.println("Correo: " + vehiculo.getCorreoElectronico());

        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo");
        }
        
        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }
        
        if (vehiculo.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de vehículo es obligatorio");
        }
        
        if (parqueaderoRepository.existeVehiculoParqueadoConPlaca(vehiculo.getPlaca())) {
            throw new IllegalStateException("Ya existe un vehículo con la placa " + vehiculo.getPlaca() + " parqueado");
        }
        
        vehiculo.setPlaca(vehiculo.getPlaca().trim().toUpperCase());
        
        ParqueaderoVehiculo vehiculoCreado = parqueaderoRepository.save(vehiculo);


        System.out.println("💾 [LOG 3] Vehículo GUARDADO en BD:");
        System.out.println("ID: " + vehiculoCreado.getId());
        System.out.println("Correo guardado: " + vehiculoCreado.getCorreoElectronico());
        
        // Enviar notificación por correo después de crear el vehículo
        SendingEmail(vehiculoCreado);
        
        return vehiculoCreado;
    }

    @Override
    public Optional<ParqueaderoVehiculo> obtenerPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return parqueaderoRepository.findById(id);
    }

    @Override
    public Optional<ParqueaderoVehiculo> obtenerPorPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return Optional.empty();
        }
        return parqueaderoRepository.findByPlacaIgnoreCase(placa.trim());
    }

    @Override
    public List<ParqueaderoVehiculo> listarTodos() {
        return parqueaderoRepository.findAll();
    }

    @Override
    public ParqueaderoVehiculo actualizar(Long id, ParqueaderoVehiculo vehiculo) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        ParqueaderoVehiculo vehiculoAActualizar = parqueaderoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el vehículo con ID: " + id));

        // Actualizar campos si vienen del DTO
        if (vehiculo.getMarca() != null) {
            vehiculoAActualizar.setMarca(vehiculo.getMarca().trim());
        }

        if (vehiculo.getModelo() != null) {
            vehiculoAActualizar.setModelo(vehiculo.getModelo().trim());
        }

        if (vehiculo.getColor() != null) {
            vehiculoAActualizar.setColor(vehiculo.getColor().trim());
        }

        if (vehiculo.getFechaSalida() != null) {
            vehiculoAActualizar.setFechaSalida(vehiculo.getFechaSalida());
        }

        // 🔥🔥🔥 IMPORTANTE: actualizar correo
        if (vehiculo.getCorreoElectronico() != null) {
            vehiculoAActualizar.setCorreoElectronico(vehiculo.getCorreoElectronico().trim());
        }

        ParqueaderoVehiculo actualizado = parqueaderoRepository.save(vehiculoAActualizar);

        // 🔥🔥🔥 OPCIONAL: enviar correo si quieres notificar el cambio
        // SendingEmail(actualizado);

        return actualizado;
    }


    @Override
    public List<ParqueaderoVehiculo> listarPorFecha(LocalDate fecha) {
        Instant inicio = fecha.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant fin = fecha.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        return parqueaderoRepository.listarPorFecha(inicio, fin);
    }

    @Override
    public List<ParqueaderoVehiculo> listarPorFechaHoy() {
        return listarPorFecha(LocalDate.now());
    }


    @Override
    public void eliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        
        if (!parqueaderoRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró el vehículo con ID: " + id);
        }
        
        parqueaderoRepository.deleteById(id);
    }

    public ParqueaderoVehiculo marcarSalida(Long id) {
        Optional<ParqueaderoVehiculo> vehiculoOpt = parqueaderoRepository.findById(id);
        if (vehiculoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el vehículo con ID: " + id);
        }
        
        ParqueaderoVehiculo vehiculo = vehiculoOpt.get();
        if (vehiculo.getFechaSalida() != null) {
            throw new IllegalStateException("El vehículo ya tiene registrada su salida");
        }
        
        vehiculo.setFechaSalida(Instant.now());
        return parqueaderoRepository.save(vehiculo);
    }
    public ParqueaderoVehiculo marcarIngreso(Long id) {
        Optional<ParqueaderoVehiculo> vehiculoOpt = parqueaderoRepository.findById(id);
        if (vehiculoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el vehículo con ID: " + id);
        }

        ParqueaderoVehiculo vehiculo = vehiculoOpt.get();
        if (vehiculo.getFechaEntrada() != null && vehiculo.getFechaSalida() == null) {
            throw new IllegalStateException("El vehículo ya tiene registrado un ingreso y no ha salido");
        }

        vehiculo.setFechaEntrada(Instant.now());
        vehiculo.setFechaSalida(null); // Por si se estaba reseteando un ingreso anterior
        return parqueaderoRepository.save(vehiculo);
    }

    public List<ParqueaderoVehiculo> obtenerVehiculosParqueados() {
        return parqueaderoRepository.findVehiculosActualmenteParqueados();
    }

    /**
     * Envía notificación por correo cuando se registra un nuevo vehículo
     * @param vehiculo El vehículo que se acaba de registrar
     */
    public void SendingEmail(ParqueaderoVehiculo vehiculo) {
        try {
            emailService.enviarNotificacionNuevoVehiculo(vehiculo);
        } catch (Exception e) {
            // Log del error pero no interrumpir el flujo principal
            System.err.println("Error al enviar correo de notificación: " + e.getMessage());
        }
    }
}
