package com.example.GragedaF_ex12_1;

import android.app.Activity;
import android.os.Bundle;

public class Preferencies extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenciesFragment()).commit();
    }
}
