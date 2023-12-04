package com.upao.entity.service;

public class Horario_Cita {

    private Long id;
    private String hora;  // Ejemplo: "08:00", "09:00", etc. Usar 0 en numeros menores a 12.
    private Boolean disponible;
    private Programacion_Cita fechaCita;
    public Horario_Cita() {
    }

    public Horario_Cita(Long id, String hora, Boolean disponible, Programacion_Cita fechaCita) {
        this.id = id;
        this.hora = hora;
        this.disponible = disponible;
        this.fechaCita = fechaCita;
    }

    public Horario_Cita(Long id) {
        this.id = id;
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

    public Programacion_Cita getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Programacion_Cita fechaCita) {
        this.fechaCita = fechaCita;
    }
}
