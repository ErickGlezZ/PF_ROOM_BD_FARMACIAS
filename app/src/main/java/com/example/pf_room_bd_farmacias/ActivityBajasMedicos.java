package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
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
    Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas_medicos);

        cajaBuscarSSN = findViewById(R.id.cajaBuscarSSN_Bajas);
        btnEliminar = findViewById(R.id.btnEliminarMedico_Bajas);
        btnEliminar.setEnabled(false);
        btnEliminar.setAlpha(0.5f);

        recycler = findViewById(R.id.recyclerResultados_Bajas);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        // Lista vacía al inicio
        adapter = new AdapterMedicos(new ArrayList<>());
        recycler.setAdapter(adapter);

        InputFilter filtroSSN = (source, start, end, dest, dstart, dend) -> {

            // Validar solo números
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    Toast.makeText(this, "Solo debes ingresar números", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }

            // Validar máximo 6
            if (dest.length() >= 6) {
                Toast.makeText(this, "Solo puedes ingresar 6 números", Toast.LENGTH_SHORT).show();
                return "";
            }

            return null;
        };

        cajaBuscarSSN.setFilters(new InputFilter[]{
                filtroSSN
        });
    }

    public void buscarMedicos(View v) {

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

            List<Medico> resultados = bd.medicoDAO().buscarPorSSN(ssn);


            runOnUiThread(() -> {
                if (resultados.isEmpty()) {
                    Toast.makeText(this, "No se encontró ningún médico", Toast.LENGTH_SHORT).show();
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


    public void eliminarMedico(View v) {

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


            Medico medico = bd.medicoDAO().buscarMedicoPorSSN(ssn);

            if (medico == null) {
                runOnUiThread(() ->
                        Toast.makeText(this, "No existe un médico con ese SSN", Toast.LENGTH_SHORT).show()
                );
                return;
            }


            int totalPacientes = bd.medicoDAO().contarPacientesDeMedico(ssn);


            runOnUiThread(() -> {

                String mensaje = "El médico " + medico.getNombre() +
                        " tiene asignados " + totalPacientes + " pacientes.\n" +
                        "Si eliminas al médico, también se eliminarán sus pacientes.\n\n" +
                        "¿Deseas continuar?";

                new android.app.AlertDialog.Builder(this)
                        .setTitle("Confirmar eliminación")
                        .setMessage(mensaje)
                        .setPositiveButton("Sí, eliminar", (dialog, which) -> {

                            // Hilo para eliminar
                            new Thread(() -> {

                                bd.medicoDAO().eliminarMedico(medico);

                                runOnUiThread(() -> {
                                    Toast.makeText(this, "Médico y pacientes eliminados", Toast.LENGTH_LONG).show();
                                    adapter.eliminarPorSSN(ssn);
                                    cajaBuscarSSN.setText("");
                                    btnEliminar.setEnabled(false);
                                    btnEliminar.setAlpha(0.5f);
                                });

                            }).start();

                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // No hacer nada
                            Toast.makeText(this, "Operación cancelada", Toast.LENGTH_SHORT).show();
                        })
                        .setCancelable(false)
                        .show();

            });

        }).start();
    }

    public void regresar(View v) {
        finish();
    }

}

