package com.example.Bio_Code.servicios;

import com.example.Bio_Code.modelo.ParqueaderoVehiculo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IParqueaderoService {
    ParqueaderoVehiculo crear(ParqueaderoVehiculo vehiculo);
    Optional<ParqueaderoVehiculo> obtenerPorId(Long id);
    Optional<ParqueaderoVehiculo> obtenerPorPlaca(String placa);
    List<ParqueaderoVehiculo> listarTodos();
    ParqueaderoVehiculo actualizar(Long id, ParqueaderoVehiculo vehiculo);
    void eliminar(Long id);
    ParqueaderoVehiculo marcarIngreso(Long id);
    ParqueaderoVehiculo marcarSalida(Long id);
    List<ParqueaderoVehiculo> obtenerVehiculosParqueados();
    List<ParqueaderoVehiculo> listarPorFecha(LocalDate fecha);
    List<ParqueaderoVehiculo> listarPorFechaHoy();


}


