package com.upao.api;

import com.upao.entity.GenericResponse;
import com.upao.entity.service.Citas;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;
import com.upao.entity.service.Agenda_Medica;
import com.upao.entity.service.Horario_Cita;
public interface CitasApi {
    String base = "/citas";

    @GET(base + "/buscarPorPaciente/{pacienteId}")
    Call<GenericResponse<List<Citas>>> buscarCitasPorPaciente(@Path("pacienteId") Integer pacienteId);

    @POST(base + "/guardar")
    Call<GenericResponse<String>> guardarCita(@Body Citas nuevaCita);

    @PUT(base + "/aplazar/{citaId}")
    Call<GenericResponse<Citas>> aplazarCita(@Path("citaId") Long citaId,
                                             @Query("nuevaFecha") String nuevaFecha,
                                             @Query("nuevaHora") String nuevaHora);

    @DELETE(base + "/eliminar/{citaId}")
    Call<GenericResponse<String>> eliminarCita(@Path("citaId") Long citaId);

    @GET(base + "/horasDisponibles")
    Call<GenericResponse<List<Horario_Cita>>> buscarHorasDisponibles(@Query("fecha") String fecha);

    @GET(base + "/fechasDisponibles")
    Call<GenericResponse<List<String>>> buscarFechasDisponibles();

    @GET(base + "/listarTodas")
    Call<GenericResponse<List<Citas>>> listarTodasLasCitas();

    @GET(base + "/obtenerPorFechaYEspecialidad")
    Call<GenericResponse<List<Agenda_Medica>>> obtenerDoctoresDisponiblesPorFechaYEspecialidad(
            @Query("fecha") String fecha,
            @Query("especialidad") String especialidad);
    @GET(base + "/citasdisponibles")
    Call<GenericResponse<List<Agenda_Medica>>> obtenerCitasDisponibles(
            @Query("fecha") String fecha);

    @GET("citas/{citaId}/especialidad")
    Call<GenericResponse<String>> buscarEspecialidadPorId(@Path("citaId") Long citaId);

}
