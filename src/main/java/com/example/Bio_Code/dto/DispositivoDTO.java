package com.example.Bio_Code.dto;

import com.example.Bio_Code.modelo.Dispositivo.TipoDispositivo;
import com.example.Bio_Code.modelo.Dispositivo.EstadoDispositivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class DispositivoDTO {

    private Long id;

    @NotBlank(message = "El nombre del dispositivo es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotNull(message = "El tipo de dispositivo es obligatorio")
    private TipoDispositivo tipo;

    @Size(max = 50, message = "La marca no puede tener más de 50 caracteres")
    private String marca;

    @Size(max = 50, message = "El modelo no puede tener más de 50 caracteres")
    private String modelo;

    @Size(max = 100, message = "El número de serie no puede tener más de 100 caracteres")
    private String numeroSerie;

    @Size(max = 50, message = "El código de inventario no puede tener más de 50 caracteres")
    private String codigoInventario;

    @NotNull(message = "El estado del dispositivo es obligatorio")
    private EstadoDispositivo estado;

    @Size(max = 100, message = "La ubicación no puede tener más de 100 caracteres")
    private String ubicacion;

    @Size(max = 100, message = "El responsable no puede tener más de 100 caracteres")
    private String responsable;

    private Instant fechaAdquisicion;
    private Instant fechaUltimoMantenimiento;
    private String observaciones;
    private Instant creadoEn;
    private Instant actualizadoEn;

    // Propiedades calculadas
    private boolean requiereMantenimiento;

    public DispositivoDTO() {
    }

    public DispositivoDTO(String nombre, TipoDispositivo tipo, EstadoDispositivo estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDispositivo getTipo() {
        return tipo;
    }

    public void setTipo(TipoDispositivo tipo) {
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

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getCodigoInventario() {
        return codigoInventario;
    }

    public void setCodigoInventario(String codigoInventario) {
        this.codigoInventario = codigoInventario;
    }

    public EstadoDispositivo getEstado() {
        return estado;
    }

    public void setEstado(EstadoDispositivo estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Instant getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Instant fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public Instant getFechaUltimoMantenimiento() {
        return fechaUltimoMantenimiento;
    }

    public void setFechaUltimoMantenimiento(Instant fechaUltimoMantenimiento) {
        this.fechaUltimoMantenimiento = fechaUltimoMantenimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public boolean isRequiereMantenimiento() {
        return requiereMantenimiento;
    }

    public void setRequiereMantenimiento(boolean requiereMantenimiento) {
        this.requiereMantenimiento = requiereMantenimiento;
    }
}




