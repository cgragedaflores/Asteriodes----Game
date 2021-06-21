package com.example.GragedaF_ex12_1;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Vector;

public class MagatzemPuntuacionsArray implements MagatzemPuntuacions{
    //Aquest valor servirà per donar nom al fitxer xml
    private static String PREFERENCIES="puntuacions";
    private Context context;

    public MagatzemPuntuacionsArray(Context context){
        this.context=context;
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        SharedPreferences preferencies=context
                .getSharedPreferences(PREFERENCIES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencies.edit();

        for (int i=9;i>=1;i--) {
            editor.putString("puntuacio"+i, preferencies.getString("puntuacio"+(i-1),""));
        }
        editor.putString("puntuacio0", punts + " " + nom);
        editor.commit();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result=new Vector<String>();
        SharedPreferences preferencies=context
                .getSharedPreferences(PREFERENCIES,Context.MODE_PRIVATE);
        for (int i=0;i<10;i++){
            String s = preferencies.getString("puntuacio"+i, "");
            if (s != null) {
                result.add(s);
            }
        }
        return result;
    }
}
