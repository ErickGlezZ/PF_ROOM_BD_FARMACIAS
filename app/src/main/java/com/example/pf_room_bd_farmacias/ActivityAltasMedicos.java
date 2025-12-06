package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;

public class ActivityAltasMedicos extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas_medicos);


        Spinner spinnerEspecialidad;
        ArrayAdapter<CharSequence> adapter;

        spinnerEspecialidad = findViewById(R.id.spinnerEspecialidad);

        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.especialidades_array,
                android.R.layout.simple_spinner_item
        );

// diseño del desplegable
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// conectar el adapter
        spinnerEspecialidad.setAdapter(adapter);

    }
}
