package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.List;

import db.FarmaciaBD;
import entities.Medico;

public class ActivityCambiosMedicos extends Activity {

    EditText cajaSSN, cajaNombre, cajaPaterno, cajaMaterno, cajaExperiencia;
    Spinner spinnerEspecialidad;
    Button btnBuscar, btnGuardar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios_medicos);

        // ENLAZAR VISTAS
        cajaSSN = findViewById(R.id.cajaSSN);
        cajaNombre = findViewById(R.id.cajaNombre);
        cajaPaterno = findViewById(R.id.cajaPaterno);
        cajaMaterno = findViewById(R.id.cajaMaterno);
        cajaExperiencia = findViewById(R.id.cajaExperiencia);
        spinnerEspecialidad = findViewById(R.id.spinnerEspecialidad);

        btnBuscar = findViewById(R.id.btnBuscarMedico_Cambios);
        btnGuardar = findViewById(R.id.btnGuardarCambiosMedico);


        // DESACTIVAR CAMPOS HASTA QUE ENCUENTRE UN SSN CORRECTO
        bloquearCamposEdicion(true);

        // DESACTIVAR GUARDAR
        btnGuardar.setEnabled(false);
        btnGuardar.setAlpha(0.5f);

        // ==== FILTROS ==== //
        cajaNombre.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaPaterno.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaMaterno.setFilters(new InputFilter[]{soloLetrasFilter});

        cajaSSN.setFilters(new InputFilter[]{filtroSSN});
        cajaExperiencia.setFilters(new InputFilter[]{filtroExperiencia});

        // ADAPTADOR SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.especialidades_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEspecialidad.setAdapter(adapter);
    }

    // BLOQUEA O DESBLOQUEA LOS CAMPOS
    private void bloquearCamposEdicion(boolean bloquear) {
        cajaNombre.setEnabled(!bloquear);
        cajaPaterno.setEnabled(!bloquear);
        cajaMaterno.setEnabled(!bloquear);
        spinnerEspecialidad.setEnabled(!bloquear);
        cajaExperiencia.setEnabled(!bloquear);

        float alpha = bloquear ? 0.5f : 1f;

        cajaNombre.setAlpha(alpha);
        cajaPaterno.setAlpha(alpha);
        cajaMaterno.setAlpha(alpha);
        spinnerEspecialidad.setAlpha(alpha);
        cajaExperiencia.setAlpha(alpha);
    }

    // =========================
    // MÉTODO BUSCAR
    // =========================
    public void buscarMedicoCambios(View v) {

        String ssn = cajaSSN.getText().toString();

        if (ssn.isEmpty()) {
            Toast.makeText(this, "Ingresa un SSN", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ssn.matches("\\d{6}")) {
            Toast.makeText(this, "El SSN debe tener 6 números", Toast.LENGTH_SHORT).show();
            return;
        }

        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {
            List<Medico> resultados = bd.medicoDAO().buscarPorSSN(ssn);

            runOnUiThread(() -> {

                if (resultados.isEmpty()) {
                    Toast.makeText(this, "No se encontró ningún médico", Toast.LENGTH_SHORT).show();

                    bloquearCamposEdicion(true);

                    btnGuardar.setEnabled(false);
                    btnGuardar.setAlpha(0.5f);

                    limpiarCamposEdicion();
                    return;
                }

                // SI EXISTE → LLENAR CAMPOS
                Medico m = resultados.get(0);

                cajaNombre.setText(m.getNombre());
                cajaPaterno.setText(m.getApePaterno());
                cajaMaterno.setText(m.getApeMaterno());
                cajaExperiencia.setText(String.valueOf(m.getAñosExperiencia()));

                // SELECCIONAR ESPECIALIDAD
                ArrayAdapter adapter = (ArrayAdapter) spinnerEspecialidad.getAdapter();
                int pos = adapter.getPosition(m.getEspecialidad());
                if (pos >= 0) spinnerEspecialidad.setSelection(pos);

                // ACTIVAR CAMPOS Y GUARDAR
                bloquearCamposEdicion(false);
                btnGuardar.setEnabled(true);
                btnGuardar.setAlpha(1f);
                cajaSSN.setEnabled(false);
                cajaSSN.setAlpha(0.5f);

                Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    // =========================
    // MÉTODO GUARDAR CAMBIOS
    // =========================
    public void guardarCambiosMedico(View v) {

        String ssn = cajaSSN.getText().toString();
        String nom = cajaNombre.getText().toString();
        String pat = cajaPaterno.getText().toString();
        String mat = cajaMaterno.getText().toString();
        String expStr = cajaExperiencia.getText().toString();
        String esp = spinnerEspecialidad.getSelectedItem().toString();

        if (nom.isEmpty() || pat.isEmpty() || mat.isEmpty() ||
                expStr.isEmpty() || spinnerEspecialidad.getSelectedItemPosition() == 0) {

            Toast.makeText(this, "Llena todos los campos correctamente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nom.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+") ||
                !pat.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+") ||
                !mat.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {

            Toast.makeText(this, "Nombre y apellidos deben tener solo letras", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!expStr.matches("\\d+")) {
            Toast.makeText(this, "Experiencia inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        int exp = Integer.parseInt(expStr);

        if (exp < 1 || exp > 60) {
            Toast.makeText(this, "La experiencia debe ser entre 1 y 60 años", Toast.LENGTH_SHORT).show();
            return;
        }

        Medico m = new Medico(ssn, nom, pat, mat, esp, exp);
        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {
            bd.medicoDAO().actualizarMedico(m);

            runOnUiThread(() -> {
                Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();

                bloquearCamposEdicion(true);
                btnGuardar.setEnabled(false);
                btnGuardar.setAlpha(0.5f);
                limpiarCamposEdicion();
                cajaSSN.setText("");
                cajaSSN.setEnabled(true);
                cajaSSN.setAlpha(1f);
            });

        }).start();
    }

    private void limpiarCamposEdicion() {
        cajaNombre.setText("");
        cajaPaterno.setText("");
        cajaMaterno.setText("");
        cajaExperiencia.setText("");
        spinnerEspecialidad.setSelection(0);
    }

    // ======================
    //   FILTROS
    // ======================
    InputFilter soloLetrasFilter = (source, start, end, dest, dstart, dend) -> {
        String permitidos = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+";
        for (int i = start; i < end; i++) {
            if (!String.valueOf(source.charAt(i)).matches(permitidos)) {
                Toast.makeText(this, "Solo letras", Toast.LENGTH_SHORT).show();
                return "";
            }
        }
        return null;
    };

    InputFilter filtroSSN = (source, start, end, dest, dstart, dend) -> {
        for (int i = start; i < end; i++) {
            if (!Character.isDigit(source.charAt(i))) {
                Toast.makeText(this, "Solo números", Toast.LENGTH_SHORT).show();
                return "";
            }
        }
        if (dest.length() >= 6) {
            Toast.makeText(this, "Máximo 6 números", Toast.LENGTH_SHORT).show();
            return "";
        }
        return null;
    };

    InputFilter filtroExperiencia = (source, start, end, dest, dstart, dend) -> {
        for (int i = start; i < end; i++) {
            if (!Character.isDigit(source.charAt(i))) {
                Toast.makeText(this, "Solo números", Toast.LENGTH_SHORT).show();
                return "";
            }
        }
        if (dest.length() >= 2) {
            Toast.makeText(this, "Máximo 2 números", Toast.LENGTH_SHORT).show();
            return "";
        }
        return null;
    };

    public void regresar(View v) {
        finish();
    }
}
