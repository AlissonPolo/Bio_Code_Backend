package com.example.Bio_Code.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "parqueadero_vehiculos")
public class ParqueaderoVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15, unique = true)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoVehiculo tipo;

    @Column(length = 50)
    private String marca;

    @Column(length = 50)
    private String modelo;

    @Column(length = 30)
    private String color;

    @Column(name = "fecha_entrada", nullable = true)
    private Instant fechaEntrada;

    @Column(name = "fecha_salida")
    private Instant fechaSalida;

    @Column(name = "correo_electronico", length = 120)
    private String correoElectronico;


    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    public ParqueaderoVehiculo() {
    }

    public ParqueaderoVehiculo(String placa, TipoVehiculo tipo) {
        this.placa = placa;
        this.tipo = tipo;
    }

    @PrePersist
    public void prePersist() {
        Instant ahora = Instant.now();
        this.creadoEn = ahora;
        this.actualizadoEn = ahora;
    }

    @PreUpdate
    public void preUpdate() {
        this.actualizadoEn = Instant.now();
    }


    public Long getId() {
        return id;
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

    public Instant getActualizadoEn() {
        return actualizadoEn;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public boolean estaParqueado() {
        return this.fechaSalida == null;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
            "id=" + id +
            ", placa='" + placa + '\'' +
            ", tipo=" + tipo +
            ", fechaEntrada=" + fechaEntrada +
            ", fechaSalida=" + fechaSalida +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParqueaderoVehiculo that)) return false;
        if (id != null && that.id != null) {
            return id.equals(that.id);
        }
        return placa != null && placa.equalsIgnoreCase(that.placa);
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : (placa != null ? placa.toLowerCase().hashCode() : 0);
    }

    public enum TipoVehiculo {
        CARRO,
        MOTO,
        CAMIONETA,
        CAMION,
        BICICLETA
    }
}
