package com.upao.activity.entity.service;

import java.util.Date;

public class ControlMedico {
    private int ID_Control;
    private Paciente paciente;
    private Medico medico;
    private Date FechaControl;
    private String Resultados;
    private byte[] Documentos;

    public int getID_Control() {
        return ID_Control;
    }

    public void setID_Control(int ID_Control) {
        this.ID_Control = ID_Control;
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
