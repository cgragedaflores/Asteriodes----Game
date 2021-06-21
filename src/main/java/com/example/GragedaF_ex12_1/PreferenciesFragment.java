package com.example.GragedaF_ex12_1;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class PreferenciesFragment extends PreferenceFragment {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencies);

        final EditTextPreference fragmentos = (EditTextPreference)findPreference(getResources().getString(R.string.pa3_key));

        fragmentos.setSummary(getResources().getString(R.string.pa3_summary) + "(" + fragmentos.getText() + ")");

        fragmentos.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue){
                int valor;
                try {
                    valor = Integer.parseInt((String)newValue);
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Ha de ser un número",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (valor>=0 && valor<=9){
                    fragmentos.setSummary(getResources().getString(R.string.pa3_summary) + "(" + valor + ")");
                    return true;
                }else {
                    Toast.makeText(getActivity(), "Máximo de fragmentos 9", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
    }
}
