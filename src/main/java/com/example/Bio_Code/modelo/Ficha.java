package com.example.Bio_Code.modelo;

import jakarta.persistence.*;


import java.io.Serializable;
@Entity
@Table(name = "ficha")
public class Ficha implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id_ficha;
    private String codigo;
    @ManyToOne
    @JoinColumn(name = "id_jornada", referencedColumnName = "id_jornada")
    private Jornada jornada;
    @ManyToOne
    @JoinColumn(name = "id_programa", referencedColumnName = "id_programa")
    private Programa programa;

    public int getId_ficha() {
        return id_ficha;
    }

    public void setId_ficha(int id_ficha) {
        this.id_ficha = id_ficha;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }
}
