package com.upao.activity.ui.MisCitas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upao.R;
import com.upao.adapter.CitasVencidasAdapter;
import com.upao.entity.service.Usuario;
import com.upao.viewmodel.CitasViewModel;

public class CitasVencidasFragment extends Fragment {

    private RecyclerView recyclerView;
    private CitasViewModel citasViewModel;
    private TextView textViewCitaInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_citas_vencidas, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCitasVencidas);
        textViewCitaInfo = view.findViewById(R.id.txtCitasVencidasInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);
        CitasVencidasAdapter citasAdapter = new CitasVencidasAdapter(citasViewModel);
        recyclerView.setAdapter(citasAdapter);

        loadData(citasAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData((CitasVencidasAdapter) recyclerView.getAdapter());
    }

    private void loadData(CitasVencidasAdapter citasAdapter) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String usuarioJson = sp.getString("UsuarioJson", null);

        if (usuarioJson != null) {
            Gson gson = new GsonBuilder().create();
            Usuario u = gson.fromJson(usuarioJson, Usuario.class);
            citasViewModel.buscarCitasVencidas().observe(getViewLifecycleOwner(), response -> {
                if (response != null) {
                    if (response.getRpta() == 1) {
                        citasAdapter.updateCitas(response.getBody());
                        if (response.getBody().isEmpty()) {
                            mostrarMensajeError("No tiene citas vencidas.");
                        }
                    } else {
                        mostrarMensajeError("Error al cargar citas: " + response.getMessage());
                    }
                } else {
                    mostrarMensajeError("Error al cargar citas: respuesta nula del servidor.");
                }
            });
        } else {
            mostrarMensajeError("Información de usuario no disponible.");
        }
    }

    private void mostrarMensajeError(String mensaje) {
        if (textViewCitaInfo != null) {
            textViewCitaInfo.setVisibility(View.VISIBLE);
            textViewCitaInfo.setText(mensaje);
        }
    }
}
