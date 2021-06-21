package com.example.GragedaF_ex12_1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MagatzemPuntuacionsJson  implements MagatzemPuntuacions{
    private String string; //Emmagatzema puntuacions en format JSON

    public MagatzemPuntuacionsJson(){
        string="";
        guardarPuntuacio(45000,"Mi nombre",System.currentTimeMillis());
        guardarPuntuacio(31000,"Otro nombre",System.currentTimeMillis());
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        //string = llegirString();
        List<Puntuacio> puntuacions = llegirJson(string);
        puntuacions.add(new Puntuacio(punts,nom,data));
        string = guardarJson(puntuacions);
        //guardarString(string);
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        //string = llegirString();
        List<Puntuacio> puntuacions = llegirJson(string);
        Vector<String> sortida = new Vector<>();
        for (Puntuacio puntuacio : puntuacions){
            sortida.add(puntuacio.getPunts()+" "+puntuacio.getNom());
        }
        return sortida;
    }

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
}
