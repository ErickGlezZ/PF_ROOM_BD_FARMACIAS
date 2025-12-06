package com.example.pf_room_bd_farmacias;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class ActivityBajasMedicos extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas_medicos);
    }


    public void regresar(View v) {
        finish();
    }
}
