package com.upao.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.upao.entity.GenericResponse;
import com.upao.entity.service.Paciente;
import com.upao.repository.PacienteRepository;
public class PacienteViewModel extends AndroidViewModel {
    private final PacienteRepository repository;

    public PacienteViewModel(@NonNull Application application) {
        super(application);
        this.repository = PacienteRepository.getInstance();
    }

    public LiveData<GenericResponse<Paciente>> guardarPaciente(Paciente p){
        return  repository.guardarPaciente(p);
    }
}
