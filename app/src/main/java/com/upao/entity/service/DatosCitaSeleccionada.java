package com.upao.entity.service;

public class DatosCitaSeleccionada {
    private int idMedico;
    private long idFecha;
    private long idHora;

    // Constructor, getters y setters
    public DatosCitaSeleccionada(int idMedico, long idFecha, long idHora) {
        this.idMedico = idMedico;
        this.idFecha = idFecha;
        this.idHora = idHora;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public long getIdFecha() {
        return idFecha;
    }

    public long getIdHora() {
        return idHora;
    }
}
