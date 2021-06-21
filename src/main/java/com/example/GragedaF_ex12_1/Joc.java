package com.example.GragedaF_ex12_1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class Joc extends AppCompatActivity {
    private VistaJoc vistaJoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);
        vistaJoc=(VistaJoc)findViewById(R.id.VistaJoc);
        vistaJoc.setPare(this);
        vistaJoc.setVistaVictoria(findViewById(R.id.Victoria));
        vistaJoc.setVistaDerrota(findViewById(R.id.Derrota));

    }
    @Override
    protected void onPause(){
        super.onPause();
        vistaJoc.getFil().pausar();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (pref.getString("entrada","1").equals("2")) {
            vistaJoc.disableSensor();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        vistaJoc.getFil().reanudar();
    }

    @Override
    protected void onDestroy(){
        vistaJoc.getFil().aturar();
        super.onDestroy();
    }
}
