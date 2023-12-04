package com.upao.api;

import com.upao.entity.service.Usuario;
import com.upao.entity.GenericResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UsuarioApi {
    //RUTA DEL CONTROLADOR USUARIO
    String base = "api/usuario";
    @FormUrlEncoded
    @POST(base+"/login")
    Call<GenericResponse<Usuario>> login(@Field("correo") String correo, @Field("clave") String clave);

    @POST(base)
    Call<GenericResponse<Usuario>> save (@Body Usuario u);

}