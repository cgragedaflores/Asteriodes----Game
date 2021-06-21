package com.example.GragedaF_ex12_1;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class MagatzemPuntuacionsSocket implements MagatzemPuntuacions{
    String ip="192.168.1.26";
    int port=4321;

    public MagatzemPuntuacionsSocket(){
        //Desactiva la comprovació d'accés a xarxa des del fil principal
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        //La connexió sempre s'ha de realitzar dins try-catch ja que la connexió pot donar error
        try {
            //Realitza connexió
            Socket sk = new Socket(ip, port);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(sk.getInputStream()));
            PrintWriter sortida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()), true);
            //Envia per xarxa una cadena
            sortida.println(punts + " " + nom);
            //Llegeix una cadena des de la xarxa
            String resposta = entrada.readLine();
            if (!resposta.equals("OK")) {
                Log.e("Asteroides", "Error: resposta del servidor incorrecte");
            }
            //Tanca la connexió
            sk.close();
        } catch (IOException e) {
            Log.e("Asteroides", e.toString(), e);
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result=new Vector<String>();
        try {
            //Realitza connexió
            Socket sk = new Socket(ip, port);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(sk.getInputStream()));
            PrintWriter sortida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()), true);
            //Envia per xarxa una cadena
            sortida.println("PUNTS");
            int n=0;
            String resposta;
            do {
                resposta=entrada.readLine();
                if (resposta!=null){
                    result.add(resposta);
                    n++;
                }
            }while (n<quantitat && resposta!=null);
            //Tanca la connexió
            sk.close();
        } catch (IOException e) {
            Log.e("Asteroides", e.toString(), e);
        }
        return result;
    }
}
