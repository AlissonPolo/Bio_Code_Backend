package com.example.Bio_Code.dto;

import java.util.Map;

public class EstadisticasDispositivosDTO {
    
    private Map<String, Long> dispositivosPorTipo;
    private Map<String, Long> dispositivosPorEstado;
    private long totalDispositivos;
    private long dispositivosActivos;
    private long dispositivosEnMantenimiento;
    private long dispositivosQueRequierenMantenimiento;
    private long dispositivosDañados;

    public EstadisticasDispositivosDTO() {
    }

    // Getters y Setters
    public Map<String, Long> getDispositivosPorTipo() {
        return dispositivosPorTipo;
    }

    public void setDispositivosPorTipo(Map<String, Long> dispositivosPorTipo) {
        this.dispositivosPorTipo = dispositivosPorTipo;
    }

    public Map<String, Long> getDispositivosPorEstado() {
        return dispositivosPorEstado;
    }

    public void setDispositivosPorEstado(Map<String, Long> dispositivosPorEstado) {
        this.dispositivosPorEstado = dispositivosPorEstado;
    }

    public long getTotalDispositivos() {
        return totalDispositivos;
    }

    public void setTotalDispositivos(long totalDispositivos) {
        this.totalDispositivos = totalDispositivos;
    }

    public long getDispositivosActivos() {
        return dispositivosActivos;
    }

    public void setDispositivosActivos(long dispositivosActivos) {
        this.dispositivosActivos = dispositivosActivos;
    }

    public long getDispositivosEnMantenimiento() {
        return dispositivosEnMantenimiento;
    }

    public void setDispositivosEnMantenimiento(long dispositivosEnMantenimiento) {
        this.dispositivosEnMantenimiento = dispositivosEnMantenimiento;
    }

    public long getDispositivosQueRequierenMantenimiento() {
        return dispositivosQueRequierenMantenimiento;
    }

    public void setDispositivosQueRequierenMantenimiento(long dispositivosQueRequierenMantenimiento) {
        this.dispositivosQueRequierenMantenimiento = dispositivosQueRequierenMantenimiento;
    }

    public long getDispositivosDañados() {
        return dispositivosDañados;
    }

    public void setDispositivosDañados(long dispositivosDañados) {
        this.dispositivosDañados = dispositivosDañados;
    }
}

