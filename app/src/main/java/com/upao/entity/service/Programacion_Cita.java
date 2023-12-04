package com.upao.entity.service;

import java.util.List;

public class Programacion_Cita {

    private Long id;

    private String fecha;    // Fecha de la cita

    private List<Horario_Cita> horasCitas;     // Lista de horas de citas asociadas a esta fecha

    public Programacion_Cita() {
    }

    public Programacion_Cita(String fecha, List<Horario_Cita> horasCitas) {
        this.fecha = fecha;
        this.horasCitas = horasCitas;
    }

    public Programacion_Cita(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Horario_Cita> getHorasCitas() {
        return horasCitas;
    }

    public void setHorasCitas(List<Horario_Cita> horasCitas) {
        this.horasCitas = horasCitas;
    }
}
