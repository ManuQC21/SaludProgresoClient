package com.upao.api;

import com.upao.entity.service.Paciente;
import com.upao.entity.GenericResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PacienteApi {
    String base = "api/paciente";
    @POST(base)
    Call<GenericResponse<Paciente>> guardarPaciente(@Body Paciente c);
}