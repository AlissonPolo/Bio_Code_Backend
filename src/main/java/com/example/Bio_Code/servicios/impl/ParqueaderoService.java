package com.example.Bio_Code.servicios.impl;

import com.example.Bio_Code.modelo.ParqueaderoVehiculo;
import com.example.Bio_Code.repositorio.impl.ParqueaderoRepository;
import com.example.Bio_Code.servicios.EmailService;
import com.example.Bio_Code.servicios.IParqueaderoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        
        Optional<ParqueaderoVehiculo> vehiculoExistente = parqueaderoRepository.findById(id);
        if (vehiculoExistente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el vehículo con ID: " + id);
        }
        
        ParqueaderoVehiculo vehiculoAActualizar = vehiculoExistente.get();
        
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
        
        return parqueaderoRepository.save(vehiculoAActualizar);
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
