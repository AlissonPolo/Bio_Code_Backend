package com.example.Bio_Code.dto;

import java.util.Date;

public class PersonaConAsistenciaDTO {
    private Integer idpersona;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String fichaCodigo;
    private String programaNombre;
    private String competenciaNombre;
    private Boolean estado;
    private String estadoAsistencia;
    private Date fechaUltimaAsistencia;

    public PersonaConAsistenciaDTO() {
    }

    public PersonaConAsistenciaDTO(Integer idpersona, String nombres, String apellidos, String correo,
                                   String telefono, String fichaCodigo, String programaNombre,
                                   String competenciaNombre, Boolean estado, String estadoAsistencia, Date fechaUltimaAsistencia) {
        this.idpersona = idpersona;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.fichaCodigo = fichaCodigo;
        this.programaNombre = programaNombre;
        this.competenciaNombre = competenciaNombre;
        this.estado = estado;
        this.estadoAsistencia = estadoAsistencia;
        this.fechaUltimaAsistencia = fechaUltimaAsistencia;


    }

    public Integer getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(Integer idpersona) {
        this.idpersona = idpersona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFichaCodigo() {
        return fichaCodigo;
    }

    public void setFichaCodigo(String fichaCodigo) {
        this.fichaCodigo = fichaCodigo;
    }

    public String getProgramaNombre() {
        return programaNombre;
    }

    public void setProgramaNombre(String programaNombre) {
        this.programaNombre = programaNombre;
    }

    public String getCompetenciaNombre() {
        return competenciaNombre;
    }

    public void setCompetenciaNombre(String competenciaNombre) {
        this.competenciaNombre = competenciaNombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getEstadoAsistencia() {
        return estadoAsistencia;
    }

    public void setEstadoAsistencia(String estadoAsistencia) {
        this.estadoAsistencia = estadoAsistencia;
    }

    public Date getFechaUltimaAsistencia() {
        return fechaUltimaAsistencia;
    }

    public void setFechaUltimaAsistencia(Date fechaUltimaAsistencia) {
        this.fechaUltimaAsistencia = fechaUltimaAsistencia;
    }
}