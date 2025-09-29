package com.example.Bio_Code.modelo;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dispositivos")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoDispositivo tipo;

    @Column(length = 50)
    private String marca;

    @Column(length = 50)
    private String modelo;

    @Column(name = "numero_serie", length = 100, unique = true)
    private String numeroSerie;

    @Column(name = "codigo_inventario", length = 50, unique = true)
    private String codigoInventario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDispositivo estado;

    @Column(length = 100)
    private String ubicacion;

    @Column(name = "responsable", length = 100)
    private String responsable;

    @Column(name = "fecha_adquisicion")
    private Instant fechaAdquisicion;

    @Column(name = "fecha_ultimo_mantenimiento")
    private Instant fechaUltimoMantenimiento;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    public Dispositivo() {
    }

    public Dispositivo(String nombre, TipoDispositivo tipo, EstadoDispositivo estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
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

    // Getters y Setters
    public Long getId() {
        return id;
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

    public Instant getActualizadoEn() {
        return actualizadoEn;
    }

    public boolean requiereMantenimiento() {
        if (fechaUltimoMantenimiento == null) {
            return true;
        }
        // Si han pasado más de 6 meses desde el último mantenimiento
        return fechaUltimoMantenimiento.isBefore(Instant.now().minusSeconds(6 * 30 * 24 * 60 * 60L));
    }

    @Override
    public String toString() {
        return "Dispositivo{" +
            "id=" + id +
            ", nombre='" + nombre + '\'' +
            ", tipo=" + tipo +
            ", estado=" + estado +
            ", ubicacion='" + ubicacion + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dispositivo that)) return false;
        if (id != null && that.id != null) {
            return id.equals(that.id);
        }
        return numeroSerie != null && numeroSerie.equals(that.numeroSerie);
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : (numeroSerie != null ? numeroSerie.hashCode() : 0);
    }

    // Enums
    public enum TipoDispositivo {
        COMPUTADORA("Computadora"),
        MONITOR("Monitor"),
        TECLADO("Teclado"),
        MOUSE("Mouse"),
        IMPRESORA("Impresora"),
        PROYECTOR("Proyector"),
        TABLET("Tablet"),
        SMARTPHONE("Smartphone"),
        ROUTER("Router"),
        SWITCH("Switch"),
        ACCESS_POINT("Access Point"),
        SERVIDOR("Servidor"),
        UPS("UPS"),
        SCANNER("Scanner"),
        CAMARA("Cámara"),
        MICROFONO("Micrófono"),
        PARLANTES("Parlantes"),
        OTROS("Otros");

        private final String displayName;

        TipoDispositivo(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum EstadoDispositivo {
        ACTIVO("Activo"),
        INACTIVO("Inactivo"),
        EN_MANTENIMIENTO("En Mantenimiento"),
        DAÑADO("Dañado"),
        FUERA_DE_SERVICIO("Fuera de Servicio"),
        EN_PRESTAMO("En Préstamo"),
        PERDIDO("Perdido"),
        DADO_DE_BAJA("Dado de Baja");

        private final String displayName;

        EstadoDispositivo(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}

