package com.example.Bio_Code.modelo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "control_asistencia")

public class Control_Asistencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private int idasistencia;

    @Column(name = "fecha_asistencia")
    private Date fechaAsistencia;
    private boolean estancia;
    private String novedad;
    @Lob
    @Column(name = "documento_excusa",columnDefinition = "MEDIUMBLOB")
    private byte[] documento_excusa;

    @ManyToOne
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    private Persona persona;
    private String descripcion;
    private String tipoArchivo;

    public int getIdasistencia() {
        return idasistencia;
    }

    public void setIdasistencia(int idasistencia) {
        this.idasistencia = idasistencia;
    }

    public Date getFechaAsistencia() {
        return fechaAsistencia;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public void setFechaAsistencia(Date fechaAsistencia) {
        this.fechaAsistencia = fechaAsistencia;
    }

    public boolean isEstancia() {
        return estancia;
    }

    public void setEstancia(boolean estancia) {
        this.estancia = estancia;
    }

    public String getNovedad() {
        return novedad;
    }

    public void setNovedad(String novedad) {
        this.novedad = novedad;
    }

    public byte[] getDocumento_excusa() {
        return documento_excusa;
    }

    public void setDocumento_excusa(byte[] documento_excusa) {
        this.documento_excusa = documento_excusa;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}