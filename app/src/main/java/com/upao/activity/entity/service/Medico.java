package com.upao.activity.entity.service;

import java.util.List;

public class Medico {
    private int ID_Medico;
    private String NombreMedico;
    private String Especialidad;
    public int getID_Medico() {
        return ID_Medico;
    }
    private List<Paciente> pacientes;

    public void setID_Medico(int ID_Medico) {
        this.ID_Medico = ID_Medico;
    }

    public String getNombreMedico() {
        return NombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        NombreMedico = nombreMedico;
    }

    public String getEspecialidad() {
        return Especialidad;
    }

    public void setEspecialidad(String especialidad) {
        Especialidad = especialidad;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }
}
