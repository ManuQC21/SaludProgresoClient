package com.upao.api;

import com.upao.entity.GenericResponse;
import com.upao.entity.service.Foto;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FotoApi {
    String base = "api/documento-almacenado";
    @Multipart
    @POST(base)
    Call<GenericResponse<Foto>> save(@Part MultipartBody.Part file, @Part("nombre") RequestBody requestBody);
}
