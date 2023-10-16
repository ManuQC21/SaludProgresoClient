package com.upao.activity.entity.service;

public class Medico {

    private int id;
    private String NombreMedico;
    private String Especialidad;

    public int getId() {
        return id;
    }

    public void setId(int ID_Medico) {
        this.id = ID_Medico;
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
}
