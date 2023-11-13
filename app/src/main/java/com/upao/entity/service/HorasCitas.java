package com.upao.entity.service;

public class HorasCitas {

    private Long id;
    private String hora;  // Ejemplo: "08:00", "09:00", etc. Usar 0 en numeros menores a 12.
    private Boolean disponible;
    private FechasCitas fechaCita;
    public HorasCitas() {
    }

    public HorasCitas(Long id, String hora, Boolean disponible, FechasCitas fechaCita) {
        this.id = id;
        this.hora = hora;
        this.disponible = disponible;
        this.fechaCita = fechaCita;
    }

// Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public FechasCitas getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(FechasCitas fechaCita) {
        this.fechaCita = fechaCita;
    }
}
