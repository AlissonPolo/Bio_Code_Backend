package com.example.Bio_Code.modelo;
import jakarta.persistence.*;

import java.io.Serializable;
@Entity
@Table(name = "acciones_rol")
public class Acciones_rol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_acciones;
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    private Rol rol;

    public int getId_acciones() {
        return id_acciones;
    }

    public void setId_acciones(int id_acciones) {
        this.id_acciones = id_acciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
