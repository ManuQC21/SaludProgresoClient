package com.upao.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.upao.api.ConfigApi;
import com.upao.api.MedicoApi;
import com.upao.entity.GenericResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicoRepository {

    private MedicoApi medicoApi;
    private MutableLiveData<String> errorLiveData;

    public MedicoRepository() {
        medicoApi = ConfigApi.getMedicoApi();
        errorLiveData = new MutableLiveData<>();
    }

    public LiveData<GenericResponse<List<String>>> getEspecialidades() {
        MutableLiveData<GenericResponse<List<String>>> data = new MutableLiveData<>();
        medicoApi.listarEspecialidades().enqueue(new Callback<GenericResponse<List<String>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<String>>> call, Response<GenericResponse<List<String>>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new GenericResponse<>(null, -1, "Error al buscar especialidades", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<String>>> call, Throwable t) {
                data.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return data;
    }


    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
