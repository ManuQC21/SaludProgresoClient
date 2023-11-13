package com.upao.entity.service;


import java.time.LocalDate;
import java.util.List;

public class FechasCitas {

    private Long id;

    private String fecha;    // Fecha de la cita

    private List<HorasCitas> horasCitas;     // Lista de horas de citas asociadas a esta fecha

    public FechasCitas() {
    }

    public FechasCitas(String fecha, List<HorasCitas> horasCitas) {
        this.fecha = fecha;
        this.horasCitas = horasCitas;
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

    public List<HorasCitas> getHorasCitas() {
        return horasCitas;
    }

    public void setHorasCitas(List<HorasCitas> horasCitas) {
        this.horasCitas = horasCitas;
    }
}
