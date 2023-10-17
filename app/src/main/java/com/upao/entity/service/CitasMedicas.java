package com.upao.entity.service;

import java.util.Date;

public class CitasMedicas {

    private int id;
    private Paciente paciente;
    private Date FechaHoraCita;
    private String AreaEspecialidad;
    private String Comentarios;
    private Medico medico;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Date getFechaHoraCita() {
        return FechaHoraCita;
    }

    public void setFechaHoraCita(Date fechaHoraCita) {
        FechaHoraCita = fechaHoraCita;
    }

    public String getAreaEspecialidad() {
        return AreaEspecialidad;
    }

    public void setAreaEspecialidad(String areaEspecialidad) {
        AreaEspecialidad = areaEspecialidad;
    }

    public String getComentarios() {
        return Comentarios;
    }

    public void setComentarios(String comentarios) {
        Comentarios = comentarios;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}
