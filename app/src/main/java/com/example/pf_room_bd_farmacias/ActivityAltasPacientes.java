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

public class ActivityAltasPacientes extends Activity {

    EditText cajaSSN, cajaNombre, cajaPaterno, cajaMaterno, cajaEdad;
    Spinner spinnerMedicoCabecera;
    TextView tvNombreMedico;

    List<Medico> listaMedicos = new ArrayList<>();   // ← Lista de médicos completa
    List<String> listaSSNMedicos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas_pacientes);

        cajaSSN = findViewById(R.id.cajaSSNPaciente);
        cajaNombre = findViewById(R.id.cajaNombrePaciente);
        cajaPaterno = findViewById(R.id.cajaPaternoPaciente);
        cajaMaterno = findViewById(R.id.cajaMaternoPaciente);
        cajaEdad = findViewById(R.id.cajaEdadPaciente);
        spinnerMedicoCabecera = findViewById(R.id.spinnerMedicoCabecera);
        tvNombreMedico = findViewById(R.id.tvNombreMedico);

        cajaNombre.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaPaterno.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaMaterno.setFilters(new InputFilter[]{soloLetrasFilter});

        InputFilter filtroSSN = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    Toast.makeText(this, "Solo debes ingresar números", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }
            if (dest.length() >= 6) {
                Toast.makeText(this, "Solo puedes ingresar 6 números", Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        };

        InputFilter filtroEdad = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    Toast.makeText(this, "Solo debes ingresar números", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }
            if (dest.length() >= 2) {
                Toast.makeText(this, "Edad máximo 2 números", Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        };

        cajaSSN.setFilters(new InputFilter[]{filtroSSN});
        cajaEdad.setFilters(new InputFilter[]{filtroEdad});

        cargarSpinnerMedicos();
    }

    // ---------------- CARGAR SPINNER CON MÉDICOS -----------------
    private void cargarSpinnerMedicos() {
        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {
            List<Medico> medicos = bd.medicoDAO().obtenerMedicos();

            listaMedicos.clear();
            listaMedicos.addAll(medicos);

            listaSSNMedicos.clear();
            listaSSNMedicos.add("Selecciona Médico...");

            for (Medico m : medicos) {
                listaSSNMedicos.add(m.getSSN());
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        listaSSNMedicos
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMedicoCabecera.setAdapter(adapter);

                // ----------- LISTENER PARA MOSTRAR EL NOMBRE DEL MÉDICO ----------
                spinnerMedicoCabecera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0) {
                            tvNombreMedico.setText("");
                            return;
                        }

                        String ssnSeleccionado = listaSSNMedicos.get(position);

                        for (Medico m : listaMedicos) {
                            if (m.getSSN().equals(ssnSeleccionado)) {
                                String mensaje = "Médico Asignado:\n" + m.getNombre();

                                tvNombreMedico.setText(mensaje);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            });
        }).start();
    }

    // ---------------------- AGREGAR PACIENTE --------------------------
    public void agregarPaciente(View v) {

        String ssn = cajaSSN.getText().toString();
        String nom = cajaNombre.getText().toString();
        String pat = cajaPaterno.getText().toString();
        String mat = cajaMaterno.getText().toString();
        String edadStr = cajaEdad.getText().toString();
        String ssnMedico = spinnerMedicoCabecera.getSelectedItem().toString();

        if (ssn.isEmpty() || nom.isEmpty() || pat.isEmpty() || mat.isEmpty()
                || edadStr.isEmpty() || spinnerMedicoCabecera.getSelectedItemPosition() == 0) {
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

        if (!edadStr.matches("\\d+")) {
            Toast.makeText(this, "Edad solo debe contener números", Toast.LENGTH_SHORT).show();
            return;
        }

        int edad = Integer.parseInt(edadStr);
        if (edad < 1 || edad > 99) {
            Toast.makeText(this, "Edad debe ser entre 1 y 99 años", Toast.LENGTH_SHORT).show();
            return;
        }

        Paciente paciente = new Paciente(ssn, nom, pat, mat, edad, ssnMedico);

        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {
            try {
                bd.pacienteDAO().agregarPaciente(paciente);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Paciente registrado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                });

            } catch (Exception e) {

                if (e.getMessage() != null && e.getMessage().contains("UNIQUE")) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Ese SSN ya está registrado", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Error al insertar: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }

            }
        }).start();
    }

    private void limpiarCampos() {
        cajaSSN.setText("");
        cajaNombre.setText("");
        cajaPaterno.setText("");
        cajaMaterno.setText("");
        cajaEdad.setText("");
        spinnerMedicoCabecera.setSelection(0);
        tvNombreMedico.setText("");

        cajaSSN.requestFocus();
    }

    private final InputFilter soloLetrasFilter = (source, start, end, dest, dstart, dend) -> {
        String permitidos = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+";

        for (int i = start; i < end; i++) {
            if (!String.valueOf(source.charAt(i)).matches(permitidos)) {
                Toast.makeText(this, "Solo debes ingresar letras", Toast.LENGTH_SHORT).show();
                return "";
            }
        }

        return null;
    };

    public void regresar(View v) {
        finish();
    }
}
