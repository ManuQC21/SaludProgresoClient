package com.upao.activity.ui.MisCitas;
/*
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.upao.R;
import com.upao.adapter.CitasAdapter;
import com.upao.api.CitasApi;
import com.upao.api.ConfigApi;
import com.upao.entity.GenericResponse;
import com.upao.entity.service.Citas;
import com.upao.entity.service.Usuario;
import com.upao.entity.service.dto.CitaConDetalleDTO;
import com.upao.utils.DateSerializer;
import com.upao.utils.TimeSerializer;
import com.upao.viewmodel.CitasViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MisCitasFragment extends Fragment {
    private CitasViewModel citasViewModel;

    private RecyclerView rcvDetalleMisCitas;

    private CitasAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_citas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initViewModel();
        initAdapter();
        guardarData();
    }

    private void init(View v) {
        rcvDetalleMisCitas = v.findViewById(R.id.rcvDetalleMisCitas);
    }

    private void initViewModel() {
        citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);
    }

    private void initAdapter() {
        CitasViewModel citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class); // Crear una nueva instancia de CitasViewModel
        adapter = new CitasAdapter(new ArrayList<>(), citasViewModel, requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE));
        rcvDetalleMisCitas.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rcvDetalleMisCitas.setAdapter(adapter);
    }


    private void guardarData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        final Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        String usuarioJson = sp.getString("UsuarioJson", null);
        if (usuarioJson != null) {
            final Usuario u = g.fromJson(usuarioJson, Usuario.class);
            this.citasViewModel.listarMisCitas(1).observe(getViewLifecycleOwner(), response -> {
                adapter.updateItems(response.getBody());
            });
        }
    }

    /*private void loadData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        final Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        String usuarioJson = sp.getString("UsuarioJson", null);
        if (usuarioJson != null) {
            final Usuario u = g.fromJson(usuarioJson, Usuario.class);
                this.citasViewModel.listarMisCitas(1).observe(getViewLifecycleOwner(), response -> {
                    if (response != null) {
                        if (response.getCode() == 200) { // Verifica el código de respuesta HTTP
                            List<CitaConDetalleDTO> citas = response.getBody();
                            if (citas != null && !citas.isEmpty()) {
                                // Convierte la lista de citas a JSON usando Gson
                                CitasJson citasJson = new CitasJson();
                                citasJson.setCitas(citas);

                                // Ahora puedes trabajar con el objeto CitasJson o el JSON resultante
                                String citasJsonString = g.toJson(citasJson);

                                // Guarda el JSON en las preferencias compartidas o donde lo necesites
                                sp.edit().putString("CitasJson", citasJsonString).apply();
                            }
                        } else {
                            // Manejar errores si la solicitud no fue exitosa
                            Log.e("MisCitasFragment", "Error en la respuesta HTTP: " + response.getMessage());
                        }
                    } else {
                        // Manejar errores si la respuesta es nula
                        Log.e("MisCitasFragment", "Respuesta nula.");
                    }
                });

        }
        }
}*/