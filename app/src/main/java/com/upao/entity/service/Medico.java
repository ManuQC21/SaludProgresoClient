package com.upao.entity.service;

public class Medico {

    private int id;
    private String nombreMedico;
    private String especialidad;

    private Foto foto;

    public Medico(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setId(int ID_Medico) {
        this.id = ID_Medico;
    }


    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }



}
