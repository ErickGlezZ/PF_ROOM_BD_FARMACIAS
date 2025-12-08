package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import db.FarmaciaBD;
import entities.Medico;
import entities.Paciente;

public class ActivityCambiosPacientes extends Activity {

    // 6 CAMPOS
    EditText cajaSSN, cajaNombre, cajaPaterno, cajaMaterno, cajaEdad;
    Spinner spinnerMedico;
    TextView tvNombreMedico;

    // Listas para spinner
    List<Medico> listaMedicos = new ArrayList<>();
    List<String> listaSSNMedicos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios_pacientes);

    }
}
