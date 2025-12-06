package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import db.FarmaciaBD;
import entities.AdapterMedicos;
import entities.Medico;

public class ActivityBajasMedicos extends Activity {

    EditText cajaBuscarSSN;
    RecyclerView recycler;
    AdapterMedicos adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas_medicos);

        cajaBuscarSSN = findViewById(R.id.cajaBuscarSSN_Bajas);

        recycler = findViewById(R.id.recyclerResultados_Bajas);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // Lista vacía al inicio
        adapter = new AdapterMedicos(new ArrayList<>());
        recycler.setAdapter(adapter);
    }

    public void buscarMedicos(View v) {

        String ssn = cajaBuscarSSN.getText().toString();

        if (ssn.isEmpty()) {
            Toast.makeText(this, "Ingresa un SSN", Toast.LENGTH_SHORT).show();
            return;
        }

        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {

            List<Medico> resultados = bd.medicoDAO().buscarPorSSN(ssn);

            runOnUiThread(() -> {
                if (resultados.isEmpty()) {
                    Toast.makeText(this, "No se encontró ningún médico", Toast.LENGTH_SHORT).show();
                }

                adapter.actualizarLista(resultados);
            });

        }).start();
    }
}

