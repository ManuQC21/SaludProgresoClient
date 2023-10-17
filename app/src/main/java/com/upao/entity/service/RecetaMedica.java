package com.upao.entity.service;

import java.util.Date;

public class RecetaMedica {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private Date FechaReceta;

    public int getId() {
        return id;
    }

    public void setId(int ID_Receta) {
        this.id = ID_Receta;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Date getFechaReceta() {
        return FechaReceta;
    }

    public void setFechaReceta(Date fechaReceta) {
        FechaReceta = fechaReceta;
    }


}
