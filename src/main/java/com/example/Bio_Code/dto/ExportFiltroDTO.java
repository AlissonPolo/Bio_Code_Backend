package com.example.Bio_Code.dto;

public class ExportFiltroDTO {
    private Integer idFicha;
    private String codigoFicha;
    private String nombreUsuario;
    private String estadoAsistencia;

    // Getters y setters
    public String getCodigoFicha() { return codigoFicha; }
    public void setCodigoFicha(String codigoFicha) { this.codigoFicha = codigoFicha; }

    public Integer getIdFicha() {
        return idFicha;
    }
    public void setIdFicha(Integer idFicha) {
        this.idFicha = idFicha;
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getEstadoAsistencia() { return estadoAsistencia; }
    public void setEstadoAsistencia(String estadoAsistencia) { this.estadoAsistencia = estadoAsistencia; }
}