package com.example.Bio_Code.dto;

import com.example.Bio_Code.modelo.ParqueaderoVehiculo.TipoVehiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class ParqueaderoVehiculoDTO {

    private Long id;

    @NotBlank(message = "La placa es obligatoria")
    @Size(max = 15, message = "La placa no puede tener más de 15 caracteres")
    private String placa;

    @NotNull(message = "El tipo de vehículo es obligatorio")
    private TipoVehiculo tipo;

    @Size(max = 50, message = "La marca no puede tener más de 50 caracteres")
    private String marca;

    @Size(max = 50, message = "El modelo no puede tener más de 50 caracteres")
    private String modelo;

    @Size(max = 30, message = "El color no puede tener más de 30 caracteres")
    private String color;

    private Instant fechaEntrada;
    private Instant fechaSalida;
    private Instant creadoEn;
    private Instant actualizadoEn;

    public ParqueaderoVehiculoDTO() {
    }

    public ParqueaderoVehiculoDTO(String placa, TipoVehiculo tipo) {
        this.placa = placa;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Instant getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Instant fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Instant getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Instant fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Instant creadoEn) {
        this.creadoEn = creadoEn;
    }

    public Instant getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(Instant actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }

    public boolean estaParqueado() {
        return this.fechaSalida == null;
    }
}
