package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import db.FarmaciaBD;
import entities.AdapterPacientes;
import entities.Paciente;

public class ActivityBajasPacientes extends Activity {

    EditText cajaBuscarSSN;
    RecyclerView recycler;
    AdapterPacientes adapter;
    Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas_pacientes);

        // Referencias UI
        cajaBuscarSSN = findViewById(R.id.cajaBuscarSSN_Pacientes_Bajas);
        btnEliminar = findViewById(R.id.btnEliminarPaciente_Bajas);

        btnEliminar.setEnabled(false);
        btnEliminar.setAlpha(0.5f);

        recycler = findViewById(R.id.recyclerResultadosPacientes_Bajas);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // Adaptador vacío al inicio
        adapter = new AdapterPacientes(new ArrayList<>());
        recycler.setAdapter(adapter);

        // VALIDAR SOLO NÚMEROS
        InputFilter soloNumeros = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    Toast.makeText(this, "Solo debes ingresar números", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }
            return null;
        };

        cajaBuscarSSN.setFilters(new InputFilter[]{
                soloNumeros,
                new InputFilter.LengthFilter(6) // SSN = 6 dígitos
        });
    }


    // ---------------------- BUSCAR PACIENTE ----------------------
    public void buscarPacienteBajas(View v) {

        String ssn = cajaBuscarSSN.getText().toString();

        if (ssn.isEmpty()) {
            Toast.makeText(this, "Ingresa un SSN", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ssn.matches("\\d{6}")) {
            Toast.makeText(this, "El SSN debe tener exactamente 6 números", Toast.LENGTH_SHORT).show();
            return;
        }

        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {

            List<Paciente> resultados = bd.pacienteDAO().buscarPorSSN(ssn);

            runOnUiThread(() -> {
                if (resultados.isEmpty()) {
                    Toast.makeText(this, "No se encontró ningún paciente", Toast.LENGTH_SHORT).show();
                    btnEliminar.setEnabled(false);
                    btnEliminar.setAlpha(0.5f);
                } else {
                    btnEliminar.setEnabled(true);
                    btnEliminar.setAlpha(1f);
                }

                adapter.actualizarLista(resultados);
            });

        }).start();
    }


    // ---------------------- ELIMINAR PACIENTE ----------------------
    public void eliminarPaciente(View v) {

        String ssn = cajaBuscarSSN.getText().toString().trim();

        if (ssn.isEmpty()) {
            Toast.makeText(this, "Debes ingresar un SSN para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ssn.matches("\\d{6}")) {
            Toast.makeText(this, "El SSN debe tener exactamente 6 números", Toast.LENGTH_SHORT).show();
            return;
        }

        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {

            Paciente paciente = bd.pacienteDAO().buscarPacientePorSSN(ssn);

            if (paciente == null) {
                runOnUiThread(() ->
                        Toast.makeText(this, "No existe un paciente con ese SSN", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            // ELIMINAR
            bd.pacienteDAO().eliminarPaciente(paciente);

            runOnUiThread(() -> {
                Toast.makeText(this, "Paciente eliminado correctamente", Toast.LENGTH_SHORT).show();

                adapter.eliminarPorSSN(ssn);

                cajaBuscarSSN.setText("");
                btnEliminar.setEnabled(false);
                btnEliminar.setAlpha(0.5f);
            });

        }).start();
    }


    public void regresar(View v) {
        finish();
    }
}
