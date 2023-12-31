package com.upao.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upao.utils.DateSerializer;
import com.upao.utils.TimeSerializer;
import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigApi {

    public static final String baseUrlE = "http://10.0.2.2:8080";
    private static Retrofit retrofit;
    private static String token="";

    private static UsuarioApi usuarioApi;
    private static PacienteApi pacienteApi;
    private static CitasApi citasApi;

    private static MedicoApi medicoApi;

    private static FotoApi documentoAlmacenadoApi;


    static {
        initClient();
    }

    private static void initClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build();

    }

    public static OkHttpClient getClient() {
        HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();
        loggin.level(HttpLoggingInterceptor.Level.BODY);

        StethoInterceptor stetho = new StethoInterceptor();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(loggin)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(stetho);
        return builder.build();
    }
    public static void setToken(String value) {
        token = value;
        initClient();
    }
    public static UsuarioApi getUsuarioApi() {
        if (usuarioApi == null) {
            usuarioApi = retrofit.create(UsuarioApi.class);
        }
        return usuarioApi;
    }
    public static PacienteApi getPacienteApi() {
        if (pacienteApi == null) {
            pacienteApi = retrofit.create(PacienteApi.class);
        }
        return pacienteApi;
    }
    public static FotoApi getDocumentoAlmacenadoApi() {
        if (documentoAlmacenadoApi == null) {
            documentoAlmacenadoApi = retrofit.create(FotoApi.class);
        }
        return documentoAlmacenadoApi;
    }
    public static CitasApi getCitasApi() {
        if (citasApi == null) {
            citasApi = retrofit.create(CitasApi.class);
        }
        return citasApi;
    }
    public static MedicoApi getMedicoApi() {
        if (medicoApi == null) {
            medicoApi = retrofit.create(MedicoApi.class);
        }
        return medicoApi;
    }

}