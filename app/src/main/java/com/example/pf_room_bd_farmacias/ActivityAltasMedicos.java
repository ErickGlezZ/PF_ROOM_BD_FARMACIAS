package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import db.FarmaciaBD;
import entities.Medico;

public class ActivityAltasMedicos extends Activity {

    EditText cajaSSN, cajaNombre, cajaPaterno, cajaMaterno, cajaExperiencia;
    Spinner spinnerEspecialidad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas_medicos);

        cajaSSN        = findViewById(R.id.cajaSSN);
        cajaNombre     = findViewById(R.id.cajaNombre);
        cajaPaterno    = findViewById(R.id.cajaPaterno);
        cajaMaterno    = findViewById(R.id.cajaMaterno);
        cajaExperiencia = findViewById(R.id.cajaExperiencia);
        spinnerEspecialidad = findViewById(R.id.spinnerEspecialidad);

        //      FILTROS
        cajaNombre.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaPaterno.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaMaterno.setFilters(new InputFilter[]{soloLetrasFilter});

        InputFilter soloNumerosFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };

        cajaSSN.setFilters(new InputFilter[]{
                soloNumerosFilter,
                new InputFilter.LengthFilter(6)   // máximo 6
        });


        cajaExperiencia.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(2)   // máximo 2 dígitos
        });

        // ADAPTADOR DEL SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.especialidades_array,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEspecialidad.setAdapter(adapter);
    }

    // MÉTODO PARA AGREGAR MÉDICO
    public void agregarMedico(View v) {

        String ssn = cajaSSN.getText().toString();
        String nom = cajaNombre.getText().toString();
        String pat = cajaPaterno.getText().toString();
        String mat = cajaMaterno.getText().toString();
        String expStr = cajaExperiencia.getText().toString();
        String especialidad = spinnerEspecialidad.getSelectedItem().toString();

        // VALIDACIONES -----------------------------

        if (ssn.isEmpty() || nom.isEmpty() || pat.isEmpty() || mat.isEmpty()
                || expStr.isEmpty() || spinnerEspecialidad.getSelectedItemPosition() == 0) {

            Toast.makeText(this, "Debes llenar todos los campos correctamente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ssn.matches("\\d{6}")) {
            Toast.makeText(this, "El SSN debe contener exactamente 6 números", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nom.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")
                || !pat.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")
                || !mat.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {

            Toast.makeText(this, "Nombre y apellidos solo deben contener letras", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!expStr.matches("\\d+")) {
            Toast.makeText(this, "La experiencia debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        int exp = Integer.parseInt(expStr);
        if (exp < 1 || exp > 60) {
            Toast.makeText(this, "La experiencia debe estar entre 1 y 60 años", Toast.LENGTH_SHORT).show();
            return;
        }


        Medico medico = new Medico(ssn, nom, pat, mat, especialidad, exp);

        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        // HILO PARA ROOM
        new Thread(() -> {
            try {

                bd.medicoDAO().agregarMedico(medico);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Médico registrado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                });

            } catch (Exception e) {

                if (e.getMessage() != null && e.getMessage().contains("UNIQUE")) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Ese SSN ya se encuentra registrado", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Error al insertar: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }

            }
        }).start();

    }

    // LIMPIAR CAMPOS
    private void limpiarCampos() {
        cajaSSN.setText("");
        cajaNombre.setText("");
        cajaPaterno.setText("");
        cajaMaterno.setText("");
        cajaExperiencia.setText("");
        spinnerEspecialidad.setSelection(0);

        cajaSSN.requestFocus();
    }

    // FILTRO SOLO LETRAS
    private final InputFilter soloLetrasFilter = (source, start, end, dest, dstart, dend) -> {
        String permitidos = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+";

        for (int i = start; i < end; i++) {
            if (!String.valueOf(source.charAt(i)).matches(permitidos)) {
                return "";
            }
        }
        return null;
    };

    public void regresar(View v) {
        finish();
    }
}
