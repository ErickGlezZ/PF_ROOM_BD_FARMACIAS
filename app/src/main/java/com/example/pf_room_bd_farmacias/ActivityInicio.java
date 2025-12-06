package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class ActivityInicio extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }


    public void abrirGestoresABCC(View view){
        Intent i = null;
        if (view.getId() == R.id.btn_medicos){
            i = new Intent(this, ActivityMedicos.class);
        } else if (view.getId() == R.id.btn_pacientes) {

        } else if (view.getId() == R.id.btn_cerrar_sesion) {
            i = new Intent(this, MainActivity.class);
        }
        startActivity(i);
    }


    public void regresar(View v) {
        finish();
    }
}
