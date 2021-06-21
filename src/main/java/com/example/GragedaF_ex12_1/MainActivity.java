package com.example.GragedaF_ex12_1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, GestureOverlayView.OnGesturePerformedListener {
    private Button btn_jugar;
    private Button btn_acercade;
    private Button btn_salir;
    private Button btn_configurar;
    public static MagatzemPuntuacions magatzem;
    private GestureLibrary gesturyLibrary;
    private TextView sortida;
    private String nombre="Anónimo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_jugar = (Button)findViewById(R.id.jugar);
        btn_jugar.setOnClickListener(this);

        btn_acercade = (Button)findViewById(R.id.acerca);
        btn_acercade.setOnClickListener(this);

        btn_salir = (Button)findViewById(R.id.salir);
        btn_salir.setOnClickListener(this);
        btn_salir.setOnLongClickListener(this);

        btn_configurar = (Button)findViewById(R.id.configurar);
        //bConfigurar.setOnClickListener(this);
        //bConfigurar.setOnLongClickListener(this);
        btn_configurar.setOnClickListener(new BconfigurarListener());
        btn_configurar.setOnLongClickListener(new BconfigurarListener());

        gesturyLibrary = GestureLibraries.fromRawResource(this,R.raw.gestures);
        if (!gesturyLibrary.load()) finish();
        GestureOverlayView gesturesView = (GestureOverlayView) findViewById(R.id.gestures);
        //associa escoltador d'event de la gestura en la mateixa classe
        gesturesView.addOnGesturePerformedListener(this);
        sortida=(TextView)findViewById(R.id.sortida);
        //Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Toast.makeText(this, "onStart", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("musica",true)) {
            startService(new Intent(MainActivity.this,ServeiMusica.class));
        }
        //Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause(){ //Se usa cuando pulso AcercaDe i Jugar
        //Toast.makeText(this, "onPause", Toast.LENGTH_LONG).show();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("musica",true)) {
            ServeiMusica.reproductor.pause();
        }
        super.onPause();
    }

    @Override
    protected void onStop(){
        //Toast.makeText(this, "onStop", Toast.LENGTH_LONG).show();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("musica",true)) {
            ServeiMusica.reproductor.pause();
        }
        super.onStop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        //Toast.makeText(this, "onRestart", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy(){
        //Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();
        super.onDestroy();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("musica",true)) {
            stopService(new Intent(MainActivity.this,ServeiMusica.class));
        }
    }

    public void llancarJugar(View view){
        Intent i=new Intent(this, Joc.class);
        //mp.pause();//El posa en pausa però si tornam enrrere no reanuda la música
        startActivityForResult(i,1234);
    }


    public void llancarAcercaDe(View view){
        Intent i=new Intent(this, AcercaDe.class);
        startActivity(i);
    }

    public void llancarConfigurar(View view){
        Intent i=new Intent(this, Preferencies.class);
        startActivity(i);
    }

    public void mostrarPreferencies(View view){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s="musica: " + pref.getBoolean("musica",false)+
                "\ntipus gràfics: " + pref.getString("graficos","1")+
                "\nnúmero fragments: " + pref.getString("fragmentos","1")+
                "\ntipus entrada: " + pref.getString("entrada","2")+
                "\nactivar multijugador: " + pref.getBoolean("multijugador",false)+
                "\nmàxim jugadors: " + pref.getString("jugadores","1")+
                "\ntipus connexió: " + pref.getString("conexion","1");

        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
    }

    public void llancarPuntuacions(View view){
        Intent i = new Intent(this, Puntuacions.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater infl=getMenuInflater();
        infl.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if (id==R.id.configuracio){
            llancarConfigurar(null);
            return true;
        }
        if (id==R.id.acercaDe){
            llancarAcercaDe(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.jugar:llancarJugar(null);break;
            case R.id.acerca:llancarAcercaDe(null);break;
            case R.id.salir:finish();break;
            case R.id.configurar:llancarConfigurar(null);break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.salir:llancarPuntuacions(null);break;
            case R.id.configurar:mostrarPreferencies(null);break;
        }
        return true;
    }

    @Override
    public void onGesturePerformed(GestureOverlayView ov, Gesture gesture){
        ArrayList<Prediction> predictions= gesturyLibrary.recognize(gesture);
        double precisio = 0;
        String gestura="";
        sortida.setText("");
        for (Prediction prediction : predictions){
            if (prediction.score>precisio){
                precisio=prediction.score;
                gestura=prediction.name;
            }
        }
        sortida.append(gestura+"\n");
        switch (gestura){
            case "jugar":llancarJugar(null);break;
            case "acercade":llancarAcercaDe(null);break;
            case "configurar":llancarConfigurar(null);break;
            case "cancelar":finish();break;
        }
    }

    private class BconfigurarListener implements View.OnClickListener, View.OnLongClickListener{
        public void onClick(View v){
            llancarConfigurar(null);
        }
        public boolean onLongClick(View v){
            mostrarPreferencies(null);
            return true;
        }
    }

    //Mètode que es crida de forma automàtica quan finalitza
    //l'activitat secundària. Permet llegir les dades retornades.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1234 && resultCode==RESULT_OK && data!=null){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            int opcion = Integer.parseInt(pref.getString("puntuaciones","1"));

            switch (opcion) {
                case 0:
                    magatzem = new MagatzemPuntuacionsArray(this);
                    break;
                case 1:
                    magatzem = new MagatzemPuntuacionsPreferencies(this);
                    break;
                case 2:
                    magatzem = new MagatzemPuntuacionsFitxerIntern(this);
                    break;
                case 3:
                    magatzem = new MagatzemPuntuacionsFitxerExtern(this);
                    break;
                case 4:
                    magatzem = new MagatzemPuntuacionsXML_SAX(this);
                    break;
                case 5:
                    magatzem = new MagatzemPuntuacionsGson();
                    break;
                case 6:
                    magatzem = new MagatzemPuntuacionsJson();
                    break;
                case 7:
                    magatzem = new MagatzemPuntuacionsSQLite(this);
                    break;
                case 8:
                    magatzem = new MagatzemPuntuacionsSQLiteRelacional(this);
                    break;
                case 9:
                    magatzem = new MagatzemPuntuacionsProvider(this);
                    break;
                case 10:
                    magatzem = new MagatzemPuntuacionsSocket();
                    break;
                case 11:
                    magatzem = new MagatzemPuntuacionsServicioWeb(this);
            }
            final int puntuacio= data.getExtras().getInt("puntuacio");
            final EditText entrada = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Nombre del jugador")
                    .setMessage("Indica su nombre : ")
                    .setView(entrada)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            nombre = entrada.getText().toString();
                            //Millor si ho llegim des d'un diàleg o una nova activitat
                            //AlertDialog.Builder
                            magatzem.guardarPuntuacio(puntuacio,nombre,System.currentTimeMillis());
                            llancarPuntuacions(null);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            nombre = "Anónimo";
                            finish();
                        }
                    })
                    .show();
        }
    }
}
