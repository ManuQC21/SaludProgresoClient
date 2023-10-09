package com.upao.activity.entity.service;

import java.util.Date;

public class Medicamentos {
    private int ID_Medicamento;
    private Paciente paciente;
    private String NombreMedicamento;
    private Date FechaInicio;
    private Date FechaFinalizacion;
    private String IndicacionesMedicas;
    private boolean RecordatorioMedicacion;

    public int getID_Medicamento() {
        return ID_Medicamento;
    }

    public void setID_Medicamento(int ID_Medicamento) {
        this.ID_Medicamento = ID_Medicamento;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getNombreMedicamento() {
        return NombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        NombreMedicamento = nombreMedicamento;
    }

    public Date getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public Date getFechaFinalizacion() {
        return FechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        FechaFinalizacion = fechaFinalizacion;
    }

    public String getIndicacionesMedicas() {
        return IndicacionesMedicas;
    }

    public void setIndicacionesMedicas(String indicacionesMedicas) {
        IndicacionesMedicas = indicacionesMedicas;
    }

    public boolean isRecordatorioMedicacion() {
        return RecordatorioMedicacion;
    }

    public void setRecordatorioMedicacion(boolean recordatorioMedicacion) {
        RecordatorioMedicacion = recordatorioMedicacion;
    }


}
