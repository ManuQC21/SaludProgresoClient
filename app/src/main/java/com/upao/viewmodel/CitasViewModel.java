package com.upao.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.upao.entity.GenericResponse;
import com.upao.entity.service.Citas;
import com.upao.entity.service.DisponibilidadMedico;
import com.upao.entity.service.HorasCitas;
import com.upao.repository.CitasRepository;

import java.util.List;

public class CitasViewModel extends AndroidViewModel {

    private final CitasRepository repository;

    public CitasViewModel(@NonNull Application application) {
        super(application);
        this.repository = CitasRepository.getInstance();
    }

    public LiveData<GenericResponse<List<Citas>>> buscarCitasPorPaciente(Integer pacienteId) {
        return repository.buscarCitasPorPaciente(pacienteId);
    }

    public LiveData<GenericResponse<String>> guardarCita(Citas nuevaCita) {
        return repository.guardarCita(nuevaCita);
    }

    public LiveData<GenericResponse<Citas>> aplazarCita(Long citaId, String nuevaFecha, String nuevaHora) {
        return repository.aplazarCita(citaId, nuevaFecha, nuevaHora);
    }

    public LiveData<GenericResponse<String>> eliminarCita(Long citaId) {
        return repository.eliminarCita(citaId);
    }

    public LiveData<GenericResponse<List<HorasCitas>>> buscarHorasDisponibles(String fecha) {
        return repository.buscarHorasDisponibles(fecha);
    }

    public LiveData<GenericResponse<List<String>>> buscarFechasDisponibles() {
        return repository.buscarFechasDisponibles();
    }

    public LiveData<GenericResponse<List<Citas>>> listarTodasLasCitas() {
        return repository.listarTodasLasCitas();
    }

    // Método para obtener citas por fecha y especialidad
    public LiveData<GenericResponse<List<DisponibilidadMedico>>> obtenerCitasPorFechaYEspecialidad(String fecha, String especialidad) {
        return repository.obtenerDoctoresDisponiblesPorFechaYEspecialidad(fecha, especialidad);
    }
}
