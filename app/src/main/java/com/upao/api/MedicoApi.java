package com.upao.api;

import com.upao.entity.GenericResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MedicoApi {

    String base = "/api/medico";

    @GET(base + "/especialidades")
    Call<GenericResponse<List<String>>> listarEspecialidades();
}
