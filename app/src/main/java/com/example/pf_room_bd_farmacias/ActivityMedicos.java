package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class ActivityMedicos extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_medicos);
    }

    public void abrirActivitiesMedico(View view){
        Intent i = null;
        if (view.getId() == R.id.btnAltasMedicos){
            i = new Intent(this, ActivityAltasMedicos.class);
        } else if (view.getId() == R.id.btnBajasMedicos) {
            i = new Intent(this, ActivityBajasMedicos.class);
        } else if (view.getId() == R.id.btnCambiosMedicos) {
            i = new Intent(this, ActivityCambiosMedicos.class);
        } else if (view.getId() == R.id.btnConsultasMedicos) {
            i = new Intent(this, ActivityConsultasMedicos.class);
        }
        startActivity(i);
    }

    public void regresar(View v) {
        finish();
    }
}
