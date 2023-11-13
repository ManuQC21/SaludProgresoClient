package com.upao.entity.service;

public class Citas {
    private Long id;
    private Paciente paciente;
    private Medico medico;
    private FechasCitas fechaCita;
    private HorasCitas horaCita;
    public Citas() {
    }
    public Citas(Long id, Paciente paciente, Medico medico, FechasCitas fechaCita, HorasCitas horaCita) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public FechasCitas getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(FechasCitas fechaCita) {
        this.fechaCita = fechaCita;
    }

    public HorasCitas getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(HorasCitas horaCita) {
        this.horaCita = horaCita;
    }
}
