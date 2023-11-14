package com.upao.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.upao.entity.GenericResponse;
import com.upao.repository.MedicoRepository;

import java.util.List;

public class MedicoViewModel extends ViewModel {
    private MedicoRepository medicoRepository;

    public MedicoViewModel() {
        medicoRepository = new MedicoRepository(); // Asegúrate de tener una instancia de tu repositorio
    }

    // Método para obtener las especialidades que devuelve un LiveData
    public LiveData<GenericResponse<List<String>>> getEspecialidades() {
        return medicoRepository.getEspecialidades();
    }

    // Método para obtener errores que devuelve un LiveData
    public LiveData<String> getErrorStream() {
        return medicoRepository.getErrorLiveData();
    }
}
