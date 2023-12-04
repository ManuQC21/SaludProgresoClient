package com.upao.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.upao.entity.service.Paciente;
import com.upao.api.PacienteApi;
import com.upao.api.ConfigApi;
import com.upao.entity.GenericResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PacienteRepository {
    private static PacienteRepository repository;
    private final PacienteApi api;

    public static PacienteRepository getInstance(){
        if(repository == null){
            repository = new PacienteRepository();
        }
        return repository;
    }
    private PacienteRepository(){
        api = ConfigApi.getPacienteApi();
    }

    public LiveData<GenericResponse<Paciente>> guardarPaciente(Paciente p){
        final MutableLiveData<GenericResponse<Paciente>> mld = new MutableLiveData<>();
        this.api.guardarPaciente(p).enqueue(new Callback<GenericResponse<Paciente>>() {
            @Override
            public void onResponse(Call<GenericResponse<Paciente>> call, Response<GenericResponse<Paciente>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Paciente>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                System.out.println("Se ha producido un error : " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}