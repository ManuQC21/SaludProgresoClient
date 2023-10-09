package com.upao.activity.entity.service;


import java.util.Date;

public class RecetaMedica {
    private int ID_Receta;
    private Paciente paciente;
    private Medico medico;
    private Medicamentos medicamentos;
    private Date FechaReceta;

    public int getID_Receta() {
        return ID_Receta;
    }

    public void setID_Receta(int ID_Receta) {
        this.ID_Receta = ID_Receta;
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

    public Medicamentos getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(Medicamentos medicamentos) {
        this.medicamentos = medicamentos;
    }

    public Date getFechaReceta() {
        return FechaReceta;
    }

    public void setFechaReceta(Date fechaReceta) {
        FechaReceta = fechaReceta;
    }


}
