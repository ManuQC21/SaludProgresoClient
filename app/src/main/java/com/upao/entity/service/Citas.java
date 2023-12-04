package com.upao.entity.service;

public class Citas {
    private Long id;
    private Paciente paciente;
    private Medico medico;
    private Programacion_Cita fechaCita;
    private Horario_Cita horaCita;
    public Citas() {
    }
    public Citas(Long id, Paciente paciente, Medico medico, Programacion_Cita fechaCita, Horario_Cita horaCita) {
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

    public Programacion_Cita getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Programacion_Cita fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Horario_Cita getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(Horario_Cita horaCita) {
        this.horaCita = horaCita;
    }
}
