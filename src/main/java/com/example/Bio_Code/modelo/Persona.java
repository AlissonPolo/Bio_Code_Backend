package com.example.Bio_Code.modelo;

import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "persona")
public class Persona implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer  idpersona;
    private String apellidos;
    private String contrasena;
    private String correo;
    @Lob
    @Column(name = "foto", columnDefinition = "LONGBLOB")
    private byte[] foto;
    private String no_documento;
    private String nombres;
    @Column(name = "telefono")
    private Long telefono;
    @ManyToOne
    @JoinColumn(name = "id_ficha", referencedColumnName = "id_ficha")
    private Ficha ficha;
    @ManyToOne
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    private Rol rol;
    @ManyToOne
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento")
    private Tipo_documento tipo_documento;
    private Boolean estado;

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Tipo_documento getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(Tipo_documento tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNo_documento() {
        return no_documento;
    }

    public void setNo_documento(String no_documento) {
        this.no_documento = no_documento;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Integer  getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(Integer idpersona) {
        this.idpersona = idpersona;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
