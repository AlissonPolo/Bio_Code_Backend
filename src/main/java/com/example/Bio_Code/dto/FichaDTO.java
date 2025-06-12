package com.example.Bio_Code.dto;

public class FichaDTO {
    private int id_ficha;
    private String codigo;

    public FichaDTO(int id_ficha, String codigo) {
        this.id_ficha = id_ficha;
        this.codigo = codigo;
    }

    public int getId_ficha() {
        return id_ficha;
    }

    public String getCodigo() {
        return codigo;
    }
}
