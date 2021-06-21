package com.example.GragedaF_ex12_1;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MagatzemPuntuacionsGson  implements MagatzemPuntuacions{
    private String string; //Emmagatzema puntuacions en format JSON
    private Gson gson = new Gson();
    private Type type = new TypeToken<Clase>() {}.getType();

    public MagatzemPuntuacionsGson(){
        //Inicialitza uns valors
        guardarPuntuacio(45000,"Mi nombre",System.currentTimeMillis());
        guardarPuntuacio(31000,"Otro nombre",System.currentTimeMillis());
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        //string = llegirString();
        /*ArrayList<Puntuacio> puntuacions;
        if (string == null){
            puntuacions = new ArrayList<>();
        }else {
            puntuacions = gson.fromJson(string,type);
        }
        puntuacions.add(new Puntuacio(punts,nom,data));
        string = gson.toJson(puntuacions,type);*/
        Clase obj;
        if (string == null){
            obj = new Clase();
        }else {
            obj= gson.fromJson(string,type);
        }
        obj.puntuacions.add(new Puntuacio(punts,nom,data));
        string = gson.toJson(obj,type);
        //guardarString(string);
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        //string = llegirString()
        /*ArrayList<Puntuacio> puntuacions;
        if (string == null){
            puntuacions = new ArrayList<>();
        }else {
            puntuacions = gson.fromJson(string,type);
        }
        Vector<String> salida = new Vector<>();
        for (Puntuacio puntuacio : puntuacions){
            salida.add(puntuacio.getPunts()+" "+puntuacio.getNom());
        }*/
        Clase objeto;
        if (string == null){
            objeto = new Clase();
        }else {
            objeto = gson.fromJson(string,type);
        }
        Vector<String> salida = new Vector<>();
        for (Puntuacio puntuacio : objeto.puntuacions){
            salida.add(puntuacio.getPunts()+" "+puntuacio.getNom());
        }

        return salida;
    }
    //Falta usar guardarJson i llegirJson!!
    private String guardarJson(List<Puntuacio> puntuacions){
        String string="";
        try {
            JSONArray jsonArray = new JSONArray();
            for (Puntuacio puntuacio : puntuacions){
                JSONObject objeto = new JSONObject();
                objeto.put("punts",puntuacio.getPunts());
                objeto.put("nom",puntuacio.getNom());
                objeto.put("data",puntuacio.getData());
                jsonArray.put(objeto);
            }
            string = jsonArray.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return string;
    }

    private List<Puntuacio> llegirJson(String string){
        List<Puntuacio> puntuacions = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i=0; i<jsonArray.length();i++){
                JSONObject objeto = jsonArray.getJSONObject(i);
                puntuacions.add(new Puntuacio(objeto.getInt("punts"),
                        objeto.getString("nom"),objeto.getLong("data")));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return puntuacions;
    }

    public class Clase{
        private ArrayList<Puntuacio> puntuacions = new ArrayList<>();
        private boolean guardat;
    }
}
