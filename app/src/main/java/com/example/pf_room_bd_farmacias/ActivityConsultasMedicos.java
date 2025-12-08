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
import entities.AdapterMedicos;
import entities.Medico;

public class ActivityConsultasMedicos extends Activity {

    SearchView cajaBuscar;
    RecyclerView recycler;
    AdapterMedicos adapter;

    List<Medico> listaOriginal = new ArrayList<>();
    List<Medico> listaFiltrada = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas_medicos);

        cajaBuscar = findViewById(R.id.cajaBuscarMedicos);
        recycler = findViewById(R.id.recyclerMedicosConsultas);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterMedicos(listaFiltrada);
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
                        return null; // se acepta
                    }

                    Toast.makeText(
                            ActivityConsultasMedicos.this,
                            "Solo puedes escribir letras y números",
                            Toast.LENGTH_SHORT
                    ).show();

                    return "";
                }
        });

        cargarTodosLosMedicos();
        configurarFiltro();
    }

    private void cargarTodosLosMedicos() {

        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {

            List<Medico> lista = bd.medicoDAO().obtenerMedicos();

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
            for (Medico m : listaOriginal) {

                if (m.getNombre().toLowerCase().contains(texto) ||
                        m.getApePaterno().toLowerCase().contains(texto) ||
                        m.getApeMaterno().toLowerCase().contains(texto) ||
                        m.getSSN().contains(texto) ||
                        m.getEspecialidad().toLowerCase().contains(texto)) {

                    listaFiltrada.add(m);
                }
            }
        }

        adapter.actualizarLista(listaFiltrada);
    }

    public void regresar(android.view.View v) {
        finish();
    }
}
