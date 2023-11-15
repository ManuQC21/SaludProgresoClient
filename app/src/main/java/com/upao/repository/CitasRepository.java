package com.upao.repository;

import com.upao.api.CitasApi;
import com.upao.api.ConfigApi;
import com.upao.entity.GenericResponse;
import com.upao.entity.service.Citas;
import com.upao.entity.service.DisponibilidadMedico;
import com.upao.entity.service.HorasCitas;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitasRepository {

    private final CitasApi api;
    private static CitasRepository repository;

    private CitasRepository() {
        this.api = ConfigApi.getCitasApi();
    }

    public static CitasRepository getInstance() {
        if (repository == null) {
            repository = new CitasRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<Citas>>> buscarCitasPorPaciente(Integer pacienteId) {
        MutableLiveData<GenericResponse<List<Citas>>> data = new MutableLiveData<>();
        api.buscarCitasPorPaciente(pacienteId).enqueue(new Callback<GenericResponse<List<Citas>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Citas>>> call, Response<GenericResponse<List<Citas>>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    // Aquí puedes manejar una respuesta no exitosa
                    data.setValue(new GenericResponse<>(null, -1, "Error en la respuesta del servidor", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Citas>>> call, Throwable t) {
                // Aquí manejas el caso de fallo en la llamada
                data.setValue(new GenericResponse<>(null, -1, "Fallo en la llamada: " + t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<GenericResponse<String>> guardarCita(Citas nuevaCita) {
        MutableLiveData<GenericResponse<String>> data = new MutableLiveData<>();
        api.guardarCita(nuevaCita).enqueue(new Callback<GenericResponse<String>>() {
            @Override
            public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    // Manejar el caso de respuesta no exitosa
                    data.setValue(new GenericResponse<>(null, -1, "Error al guardar la cita", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<String>> call, Throwable t) {
                // Manejar el caso de fallo en la llamada
                data.setValue(new GenericResponse<>(null, -1, "Error en la red: " + t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<GenericResponse<Citas>> aplazarCita(Long citaId, String nuevaFecha, String nuevaHora) {
        MutableLiveData<GenericResponse<Citas>> data = new MutableLiveData<>();
        api.aplazarCita(citaId, nuevaFecha, nuevaHora).enqueue(new Callback<GenericResponse<Citas>>() {
            @Override
            public void onResponse(Call<GenericResponse<Citas>> call, Response<GenericResponse<Citas>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    String errorMsg = response.errorBody() != null ? response.errorBody().toString() : "Respuesta no exitosa";
                    data.setValue(new GenericResponse<>(null, -1, "Error al aplazar la cita: " + errorMsg, null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<Citas>> call, Throwable t) {
                data.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return data;
    }

    // Método para eliminar una cita
    public LiveData<GenericResponse<String>> eliminarCita(Long citaId) {
        MutableLiveData<GenericResponse<String>> data = new MutableLiveData<>();
        api.eliminarCita(citaId).enqueue(new Callback<GenericResponse<String>>() {
            @Override
            public void onResponse(Call<GenericResponse<String>> call, Response<GenericResponse<String>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    String errorMsg = "Error al eliminar la cita";
                    data.setValue(new GenericResponse<>(null, -1, errorMsg, null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<String>> call, Throwable t) {
                data.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return data;
    }

    // Método para buscar horas disponibles
    public LiveData<GenericResponse<List<HorasCitas>>> buscarHorasDisponibles(String fecha) {
        MutableLiveData<GenericResponse<List<HorasCitas>>> data = new MutableLiveData<>();
        api.buscarHorasDisponibles(fecha).enqueue(new Callback<GenericResponse<List<HorasCitas>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<HorasCitas>>> call, Response<GenericResponse<List<HorasCitas>>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new GenericResponse<>(null, -1, "Error al buscar horas disponibles", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<HorasCitas>>> call, Throwable t) {
                data.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return data;
    }

    // Método para buscar fechas disponibles
    public LiveData<GenericResponse<List<String>>> buscarFechasDisponibles() {
        MutableLiveData<GenericResponse<List<String>>> data = new MutableLiveData<>();
        api.buscarFechasDisponibles().enqueue(new Callback<GenericResponse<List<String>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<String>>> call, Response<GenericResponse<List<String>>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new GenericResponse<>(null, -1, "Error al buscar fechas disponibles", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<String>>> call, Throwable t) {
                data.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return data;
    }

    // Método para listar todas las citas
    public LiveData<GenericResponse<List<Citas>>> listarTodasLasCitas() {
        MutableLiveData<GenericResponse<List<Citas>>> data = new MutableLiveData<>();
        api.listarTodasLasCitas().enqueue(new Callback<GenericResponse<List<Citas>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Citas>>> call, Response<GenericResponse<List<Citas>>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new GenericResponse<>(null, -1, "Error al listar citas", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Citas>>> call, Throwable t) {
                data.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });
        return data;
    }

    // Método para obtener citas por fecha y especialidad
    public LiveData<GenericResponse<List<DisponibilidadMedico>>> obtenerDoctoresDisponiblesPorFechaYEspecialidad(String fecha, String especialidad) {
        final MutableLiveData<GenericResponse<List<DisponibilidadMedico>>> data = new MutableLiveData<>();

        api.obtenerDoctoresDisponiblesPorFechaYEspecialidad(fecha, especialidad).enqueue(new Callback<GenericResponse<List<DisponibilidadMedico>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<DisponibilidadMedico>>> call, Response<GenericResponse<List<DisponibilidadMedico>>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new GenericResponse<>(null, -1, "Error al obtener doctores disponibles", null));
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<List<DisponibilidadMedico>>> call, Throwable t) {
                data.setValue(new GenericResponse<>(null, -1, "Fallo en la conexión: " + t.getMessage(), null));
            }
        });

        return data;
    }

}
