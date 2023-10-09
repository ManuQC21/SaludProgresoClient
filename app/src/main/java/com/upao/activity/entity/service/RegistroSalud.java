package com.upao.activity.entity.service;

import java.math.BigDecimal;
import java.util.Date;


public class RegistroSalud {
    private int ID_Registro;
    private Paciente paciente;
    private Date fechaRegistro;
    private BigDecimal peso;
    private String presionArterial;
    private BigDecimal nivelGlucosa;

    public int getID_Registro() {
        return ID_Registro;
    }

    public void setID_Registro(int ID_Registro) {
        this.ID_Registro = ID_Registro;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getPresionArterial() {
        return presionArterial;
    }

    public void setPresionArterial(String presionArterial) {
        this.presionArterial = presionArterial;
    }

    public BigDecimal getNivelGlucosa() {
        return nivelGlucosa;
    }

    public void setNivelGlucosa(BigDecimal nivelGlucosa) {
        this.nivelGlucosa = nivelGlucosa;
    }


}
