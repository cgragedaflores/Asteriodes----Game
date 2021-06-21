package com.example.GragedaF_ex12_1;

import android.content.Context;
import android.os.StrictMode;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class MagatzemPuntuacionsServicioWeb implements MagatzemPuntuacions{
    private Context context;
    private Vector<String> res = new Vector<String>();

    public MagatzemPuntuacionsServicioWeb(Context context) {
        this.context = context;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        System.out.println("URL");
        try {
            URL url = new URL("http://192.168.1.212:8081/puntuacions/novapuntuacio?punts="+punts+"&nom="+nom+"&data="+data);

            //Cream connexió
            URLConnection connexio = url.openConnection();
            new InputStreamReader(connexio.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        BufferedReader reader;
        JSONArray jsonArray;
        JSONObject json = null;
        try {
            URL url = new URL("http://192.168.1.212:8081/puntuacions/llista");
            URLConnection connexio = url.openConnection();

            reader = new BufferedReader(new InputStreamReader(connexio.getInputStream(), "UTF-8"));

            String linia;
            StringBuffer resposta = new StringBuffer();
            while ((linia = reader.readLine()) != null) {
                resposta.append(linia);
            }

            jsonArray = new JSONArray(resposta.toString());
            //Llegiem la informació que ens interesa del JSON
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj1 = null;
                try {
                    obj1 = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                res.add(obj1.optLong("punts") + " " + obj1.optString("nom"));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
