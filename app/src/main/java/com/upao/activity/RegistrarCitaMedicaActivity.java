package com.upao.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.upao.R;

public class RegistrarCitaMedicaActivity extends AppCompatActivity {

    private EditText edtFechaCita;
    private EditText edtHoraCita;
    private EditText edtEspecialidad;
    private EditText edtComentario;
    private Button btnRegistrarCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cita_medica);
    }
}