package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import db.FarmaciaBD;
import entities.AdapterPacientes;
import entities.Paciente;

public class ActivityConsultasPacientes extends Activity {

    SearchView cajaBuscar;
    RecyclerView recycler;
    AdapterPacientes adapter;

    List<Paciente> listaOriginal = new ArrayList<>();
    List<Paciente> listaFiltrada = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas_pacientes);

        cajaBuscar = findViewById(R.id.cajaBuscarPacientes);
        recycler = findViewById(R.id.recyclerPacientesConsultas);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterPacientes(listaFiltrada);
        recycler.setAdapter(adapter);

        int id = cajaBuscar.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);

        EditText searchEditText = cajaBuscar.findViewById(id);

        searchEditText.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {

                    if (source.length() == 0) {
                        return null;
                    }
                    
                    String permitido = "[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+";

                    if (source.toString().matches(permitido)) {
                        return null; // aceptar
                    }

                    Toast.makeText(
                            ActivityConsultasPacientes.this,
                            "Solo puedes escribir letras y números",
                            Toast.LENGTH_SHORT
                    ).show();

                    return ""; // bloquear
                }
        });

        cargarTodosLosPacientes();
        configurarFiltro();
    }

    private void cargarTodosLosPacientes() {

        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {

            List<Paciente> lista = bd.pacienteDAO().obtenerPacientes();

            runOnUiThread(() -> {
                listaOriginal.clear();
                listaOriginal.addAll(lista);

                listaFiltrada.clear();
                listaFiltrada.addAll(lista);

                adapter.actualizarLista(listaFiltrada);
            });

        }).start();
    }

    private void configurarFiltro() {

        cajaBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                filtrar(texto);
                return true;
            }
        });
    }

    private void filtrar(String texto) {

        texto = texto.trim().toLowerCase();
        listaFiltrada.clear();

        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {

            for (Paciente p : listaOriginal) {

                if (p.getNombre().toLowerCase().contains(texto) ||
                        p.getApePaterno().toLowerCase().contains(texto) ||
                        p.getApeMaterno().toLowerCase().contains(texto) ||
                        p.getSSN().contains(texto) ||
                        String.valueOf(p.getEdad()).contains(texto) ||
                        p.getSsnMedicoCabecera().contains(texto)) {

                    listaFiltrada.add(p);
                }
            }
        }

        adapter.actualizarLista(listaFiltrada);
    }

    public void regresar(android.view.View v) {
        finish();
    }
}
