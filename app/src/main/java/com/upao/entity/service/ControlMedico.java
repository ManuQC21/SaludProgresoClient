package com.upao.entity.service;

import java.util.Date;

public class ControlMedico {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private Date FechaControl;
    private String Resultados;
    private byte[] Documentos;

    public int getId() {
        return id;
    }

    public void setId(int ID_Control) {
        this.id = ID_Control;
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

    public Date getFechaControl() {
        return FechaControl;
    }

    public void setFechaControl(Date fechaControl) {
        FechaControl = fechaControl;
    }

    public String getResultados() {
        return Resultados;
    }

    public void setResultados(String resultados) {
        Resultados = resultados;
    }

    public byte[] getDocumentos() {
        return Documentos;
    }

    public void setDocumentos(byte[] documentos) {
        Documentos = documentos;
    }

}
