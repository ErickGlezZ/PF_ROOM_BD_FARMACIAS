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

        cajaSSN = findViewById(R.id.cajaSSN_Pac);
        cajaNombre = findViewById(R.id.cajaNombre_Pac);
        cajaPaterno = findViewById(R.id.cajaPaterno_Pac);
        cajaMaterno = findViewById(R.id.cajaMaterno_Pac);
        cajaEdad = findViewById(R.id.cajaEdad_Pac);
        spinnerMedico = findViewById(R.id.spinnerSSNMedCab_Pac);
        tvNombreMedico = findViewById(R.id.tvNombreMedico_Pac);

        // FILTROS
        cajaNombre.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaPaterno.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaMaterno.setFilters(new InputFilter[]{soloLetrasFilter});
        cajaSSN.setFilters(new InputFilter[]{filtroSSN});
        cajaEdad.setFilters(new InputFilter[]{filtroEdad});

        bloquearCampos(true);

        cargarSpinnerMedicos();
    }

    // ===========================================================
    // CARGA MÉDICOS EN EL SPINNER
    // ===========================================================
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
                spinnerMedico.setAdapter(adapter);

                spinnerMedico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0) {
                            tvNombreMedico.setText("");
                            return;
                        }

                        String ssnSel = listaSSNMedicos.get(position);

                        for (Medico m : listaMedicos) {
                            if (m.getSSN().equals(ssnSel)) {
                                tvNombreMedico.setText(m.getNombre());
                                break;
                            }
                        }
                    }
                    @Override public void onNothingSelected(AdapterView<?> parent) {}
                });
            });

        }).start();
    }

    // ===========================================================
    // BUSCAR PACIENTE
    // ===========================================================
    public void buscarPacienteCambios(View v) {

        String ssn = cajaSSN.getText().toString();

        if (!ssn.matches("\\d{6}")) {
            Toast.makeText(this, "Ingresa un SSN válido de 6 números", Toast.LENGTH_SHORT).show();
            return;
        }

        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {

            Paciente p = bd.pacienteDAO().buscarPacientePorSSN(ssn);

            runOnUiThread(() -> {

                if (p == null) {
                    Toast.makeText(this, "Paciente no encontrado", Toast.LENGTH_SHORT).show();
                    bloquearCampos(true);
                    limpiarCampos();
                    return;
                }

                // LLENAR CAMPOS
                cajaNombre.setText(p.getNombre());
                cajaPaterno.setText(p.getApePaterno());
                cajaMaterno.setText(p.getApeMaterno());
                cajaEdad.setText(String.valueOf(p.getEdad()));

                // SELECCIONAR MÉDICO EN SPINNER
                int pos = listaSSNMedicos.indexOf(p.getSsnMedicoCabecera());
                spinnerMedico.setSelection(pos >= 0 ? pos : 0);

                bloquearCampos(false);
                cajaSSN.setEnabled(false);   // Evitar cambiar SSN
                cajaSSN.setAlpha(0.5f);

                Toast.makeText(this, "Paciente encontrado", Toast.LENGTH_SHORT).show();
            });

        }).start();
    }

    // ===========================================================
    // GUARDAR CAMBIOS
    // ===========================================================
    public void guardarCambiosPaciente(View v) {

        String ssn = cajaSSN.getText().toString();
        String nom = cajaNombre.getText().toString();
        String pat = cajaPaterno.getText().toString();
        String mat = cajaMaterno.getText().toString();
        String edadStr = cajaEdad.getText().toString();
        String ssnMedico = spinnerMedico.getSelectedItem().toString();

        if (nom.isEmpty() || pat.isEmpty() || mat.isEmpty() ||
                edadStr.isEmpty() || spinnerMedico.getSelectedItemPosition() == 0) {

            Toast.makeText(this, "Llena todos los campos correctamente", Toast.LENGTH_SHORT).show();
            return;
        }

        int edad = Integer.parseInt(edadStr);

        if (edad < 1 || edad > 99) {
            Toast.makeText(this, "Edad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        Paciente pac = new Paciente(ssn, nom, pat, mat, edad, ssnMedico);

        FarmaciaBD bd = FarmaciaBD.getAppDatabase(getBaseContext());

        new Thread(() -> {
            bd.pacienteDAO().actualizarPaciente(pac);

            runOnUiThread(() -> {
                Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                bloquearCampos(true);

                cajaSSN.setEnabled(true);
                cajaSSN.setAlpha(1f);
            });

        }).start();
    }

    // ===========================================================
    // UTILIDADES
    // ===========================================================

    private void bloquearCampos(boolean bloquear) {
        cajaNombre.setEnabled(!bloquear);
        cajaPaterno.setEnabled(!bloquear);
        cajaMaterno.setEnabled(!bloquear);
        cajaEdad.setEnabled(!bloquear);
        spinnerMedico.setEnabled(!bloquear);

        float alpha = bloquear ? 0.5f : 1f;

        cajaNombre.setAlpha(alpha);
        cajaPaterno.setAlpha(alpha);
        cajaMaterno.setAlpha(alpha);
        cajaEdad.setAlpha(alpha);
        spinnerMedico.setAlpha(alpha);
    }

    private void limpiarCampos() {
        cajaNombre.setText("");
        cajaPaterno.setText("");
        cajaMaterno.setText("");
        cajaEdad.setText("");
        spinnerMedico.setSelection(0);
        tvNombreMedico.setText("");
    }

    // FILTROS
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
        if (dest.length() >= 6) return "";
        return null;
    };

    InputFilter filtroEdad = (source, start, end, dest, dstart, dend) -> {
        for (int i = start; i < end; i++) {
            if (!Character.isDigit(source.charAt(i))) return "";
        }
        if (dest.length() >= 2) return "";
        return null;
    };

    public void regresar(View v) { finish(); }
}
