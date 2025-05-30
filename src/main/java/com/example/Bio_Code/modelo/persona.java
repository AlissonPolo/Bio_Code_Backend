package com.example.Bio_Code.modelo;

import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "persona")
public class persona implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_persona;
    private String nombres;
    private String apellidos;
    @Column(name = "telefono")
    private Long telefono;
    private String no_documento;
    private String correo;
    @Lob
    @Column(name = "foto", columnDefinition = "LONGBLOB")
    private byte[] foto;
    @Lob
    @Column(name = "huella")
    private byte[] huella;
    @ManyToOne
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento")
    private Tipo_documento tipo_documento;
    @ManyToOne
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    private Rol rol;
    @ManyToOne
    @JoinColumn(name = "id_ficha", referencedColumnName = "id_ficha")
    private Ficha ficha;
    private String contrasena;
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

    public byte[] getHuella() {
        return huella;
    }

    public void setHuella(byte[] huella) {
        this.huella = huella;
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

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
