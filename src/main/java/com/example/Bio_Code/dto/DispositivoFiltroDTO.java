package com.example.Bio_Code.dto;

import com.example.Bio_Code.modelo.Dispositivo.TipoDispositivo;
import com.example.Bio_Code.modelo.Dispositivo.EstadoDispositivo;

public class DispositivoFiltroDTO {
    
    private String nombre;
    private TipoDispositivo tipo;
    private EstadoDispositivo estado;
    private String ubicacion;
    private String responsable;
    private String marca;

    public DispositivoFiltroDTO() {
    }

    // Getters y Setters
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}




