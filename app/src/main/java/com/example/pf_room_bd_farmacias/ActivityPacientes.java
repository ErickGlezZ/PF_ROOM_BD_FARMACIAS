package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class ActivityPacientes extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_pacientes);
    }

    public void abrirActivitiesPaciente(View view){
        Intent i = null;
        if (view.getId() == R.id.btnAltasPacientes){
            i = new Intent(this, ActivityAltasPacientes.class);
        } else if (view.getId() == R.id.btnBajasPacientes) {
            i = new Intent(this, ActivityBajasPacientes.class);
        } else if (view.getId() == R.id.btnCambiosPacientes) {
            i = new Intent(this, ActivityCambiosPacientes.class);
        } /*else if (view.getId() == R.id.btnConsultasPacientes) {
            i = new Intent(this, ActivityConsultasMedicos.class);
        }

         */
        startActivity(i);
    }

    public void regresar(View v) {
        finish();
    }
}
