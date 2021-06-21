package com.example.GragedaF_ex12_1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Vector;

public class VistaJoc extends View implements SensorEventListener {
    //Variables per ASTEROIDES
    private Vector<Grafic> asteroides; //Vector amb els asteroides
    private int numAsteroides = 5; //Número inicial de Asteroides
    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
    private int numFragmentos = Integer.parseInt(pref.getString("fragmentos","3")); //Fragments en que es divideix
    //Variables per la NAU
    private Grafic nau; //Gràfic de la nau
    private int girNau; //Increment de direcció
    private double acceleracioNau; //Augment de velocitat
    private static final int MAX_VELOCITAT_NAU = 20;
    //increment estandard de gir i acceleracio
    private static final int PAS_GIR_NAU = 5;
    private static final float PAS_ACCELERACIO_NAU = 0.5f;
    //FILS I TEMPS
    //Fil encarregat de processar el joc
    private ThreadJoc fil = new ThreadJoc();
    //Cada quan volem processar canvis (ms)
    private static int PERIODE_PROCES=50;
    //Quan es va realitzar el darrer procés
    private long darrerProces=0;
    //Variables pel Missil
    private Vector<Grafic> missils;
    private static int PAS_VELOCITAT_MISSIL=12;
    private Vector<Integer> tempsMissil;
    private Drawable drawableMissil;
    private Drawable drawableAsteroide[]=new Drawable[3];
    private SensorManager mSensorManager;
    //Variables pel so
    SoundPool soundPool;
    int idDispar,idExplosio;
    //Variable que conté la puntuació del joc
    private int puntuacio=0;
    //Objecte que permet accedir a l'activitat que crida al Layout
    private Activity pare;
    //Variables que indiquen l'estat del joc
    public static final int ESTAT_JUGANT=0;
    public static final int ESTAT_VICTORIA=1;
    public static final int ESTAT_DERROTA=2;

    private int estat=ESTAT_JUGANT;
    private View vistaVictoria;
    private View vistaDerrota;

    public void setPare(Activity pare){
        this.pare = pare;
    }

    public VistaJoc(Context context, AttributeSet attrs){
        super(context,attrs);
        //Declara i obté les imatges
        Drawable drawableNau;
        //drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(pref.getString("graficos","1").equals("0")){
            Path pathAsteroide = new Path();
            pathAsteroide.moveTo((float)0.3, (float)0.0);
            pathAsteroide.lineTo((float)0.6, (float)0.0);
            pathAsteroide.lineTo((float)0.6, (float)0.3);
            pathAsteroide.lineTo((float)0.8, (float)0.2);
            pathAsteroide.lineTo((float)1.0, (float)0.4);
            pathAsteroide.lineTo((float)0.8, (float)0.6);
            pathAsteroide.lineTo((float)0.9, (float)0.9);
            pathAsteroide.lineTo((float)0.8, (float)1.0);
            pathAsteroide.lineTo((float)0.4, (float)1.0);
            pathAsteroide.lineTo((float)0.0, (float)0.6);
            pathAsteroide.lineTo((float)0.0, (float)0.2);
            pathAsteroide.lineTo((float)0.3, (float)0.0);
            for (int i=0;i<3;i++) {
                ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAsteroide, 1, 1));
                dAsteroide.getPaint().setColor(Color.WHITE);
                dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
                dAsteroide.setIntrinsicWidth(50-i*14);
                dAsteroide.setIntrinsicHeight(50-i*14);
                drawableAsteroide[i] = dAsteroide;
            }
            setBackgroundColor(Color.BLACK);
            Path pathNau = new Path();
            pathNau.lineTo((float)0.0, (float)0.0);
            pathNau.lineTo((float)1.0, (float)0.5);
            pathNau.lineTo((float)0.0, (float)1.0);
            pathNau.lineTo((float)0.0, (float)0.7);
            pathNau.lineTo((float)0.2, (float)0.5);
            pathNau.lineTo((float)0.0, (float)0.3);
            pathNau.lineTo((float)0.0, (float)0.0);
            ShapeDrawable dNau = new ShapeDrawable(new PathShape(pathNau,1,1));
            dNau.getPaint().setColor(Color.WHITE);
            dNau.getPaint().setStyle(Paint.Style.STROKE);
            dNau.setIntrinsicWidth(100);
            dNau.setIntrinsicHeight(50);
            drawableNau = dNau;
            //Gràfic vectorial Missil
            ShapeDrawable dMissil = new ShapeDrawable(new RectShape());
            dMissil.getPaint().setColor(Color.WHITE);
            dMissil.getPaint().setStyle(Paint.Style.STROKE);
            dMissil.setIntrinsicWidth(15);
            dMissil.setIntrinsicHeight(3);
            drawableMissil=dMissil;
        }else{
            drawableAsteroide[0] = context.getResources().getDrawable(R.drawable.ic_asteroid__1_);
            drawableAsteroide[1] = context.getResources().getDrawable(R.drawable.ic_asteroid__2_);
            drawableAsteroide[2] = context.getResources().getDrawable(R.drawable.ic_asteroid__3_);
            drawableNau = context.getResources().getDrawable(R.drawable.spaceship);
            drawableMissil=context.getResources().getDrawable(R.drawable.ic_misil__1_);
        }
        //Inicialitza la nau
        nau = new Grafic(this,drawableNau);
        //Inicialitza els asteroides
        asteroides = new Vector<Grafic>();
        for (int i=0;i<numAsteroides;i++){
            Grafic asteroide = new Grafic(this,drawableAsteroide[0]);
            asteroide.setIncY(Math.random()*4-2);
            asteroide.setIncX(Math.random()*4-2);
            asteroide.setAngle((int)(Math.random()*360));
            asteroide.setRotacio((int)(Math.random()*8-4));
            asteroides.add(asteroide);
        }
        //Inicialitza el missil
        missils=new Vector<Grafic>();
        tempsMissil=new Vector<Integer>();
        //Habilitar sensor si està la opció marcada
        if (pref.getString("entrada","1").equals("2")) {
            //Registre el sensor d'orientació i indica gestió d'events
            activateSensor(context);
        }
        //Inicialitza so
        soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        idDispar=soundPool.load(context,R.raw.dispar,0);
        idExplosio=soundPool.load(context,R.raw.explosio,0);
    }

    public void setVistaDerrota(View vista){
        vistaDerrota=vista;
    }

    public void setVistaVictoria(View vista){
        vistaVictoria=vista;
    }

    public void activateSensor(Context context){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> llistaSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!llistaSensors.isEmpty()) {
            Sensor accelerometreSensor = llistaSensors.get(0);
            mSensorManager.registerListener(this, accelerometreSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void disableSensor(){
        if (mSensorManager!=null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onSizeChanged(int ample, int alt, int ample_ant, int alt_ant){
        super.onSizeChanged(ample,alt,ample_ant,alt_ant);
        //Una vegada que coneixem la nostra amplada i altura
        //Posiciona al centre la nau
        nau.setCenX(ample/2);
        nau.setCenY(alt/2);
        //Posiciona els asteroides
        for (Grafic asteroide : asteroides){
            do {
                asteroide.setCenX((int) (Math.random() * ample));
                asteroide.setCenY((int) (Math.random() * alt));
            }while (asteroide.distancia(nau) < (ample+alt)/5);
        }
        //Llança un nou fil
        darrerProces = System.currentTimeMillis();
        fil.start();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //Dibuixa els asteroides
        synchronized (asteroides) {
            for (Grafic asteroide : asteroides) {
                asteroide.dibuixaGrafic(canvas);
            }
        }
        //Dibuixa la nau
        nau.dibuixaGrafic(canvas);
        //Dibuixa el missil
            if (!missils.isEmpty()) {
                synchronized (missils) {
                    for (Grafic missil : missils) {
                        missil.dibuixaGrafic(canvas);
                    }
                }
            }
        //Mostra la vista de Victòria
        if (estat==ESTAT_VICTORIA){
            vistaVictoria.setVisibility(VISIBLE);
        }

        if (estat==ESTAT_DERROTA){
            vistaDerrota.setVisibility(VISIBLE);
        }
    }

    //ACTUALITZA ELS VALORS DELS ELEMENTS, ES A DIR, GESTIONA ELS MOVIMENTS
    protected void actualitzaFisica(){
        if (estat==ESTAT_JUGANT) {
            //Hora actual en milisegons
            long ara = System.currentTimeMillis();
            //No fer res si el periode de proces NO s'ha complert
            if (darrerProces + PERIODE_PROCES > ara) {
                return;
            }
            //Per una execució en temps real calculem retard
            double retard = (ara - darrerProces) / PERIODE_PROCES;
            darrerProces = ara; //Per la propera vegada
            //Actualitzem velocitat i direcció de la nau a partir de girNau i acceleracioNau segons l'entrada del jugador
            nau.setAngle((int) (nau.getAngle() + girNau * retard));
            double nIncX = nau.getIncX() + acceleracioNau * Math.cos(Math.toRadians(nau.getAngle())) * retard;
            double nIncY = nau.getIncY() + acceleracioNau * Math.sin(Math.toRadians(nau.getAngle())) * retard;
            //Actualitzem si el mòdul de la velocitat no passa el màxim
            if (Math.hypot(nIncX, nIncY) <= MAX_VELOCITAT_NAU) {
                nau.setIncX(nIncX);
                nau.setIncY(nIncY);
            }
            //Actualitzem les posicions X i Y
            nau.incrementaPos(retard);
            synchronized (asteroides) {
                for (int i = 0; i < asteroides.size(); i++) {
                    asteroides.get(i).incrementaPos(retard);
                }
            }
            //Actualitza posició del missil
            if (!missils.isEmpty()) {
                synchronized (missils) {
                    for (int i = 0; i < missils.size(); i++) {
                        missils.get(i).incrementaPos(retard);
                        tempsMissil.set(i, tempsMissil.get(i) - (int) retard);
                        if (tempsMissil.get(i) < 0) {
                            missils.remove(i);
                            tempsMissil.remove(i);
                            break;
                        } else {
                            for (int x = 0; x < asteroides.size(); x++) {
                                if (missils.get(i).verificaColisio(asteroides.get(x))) {
                                    destrueixAsteroide(x);
                                    missils.remove(i);
                                    tempsMissil.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            //Si un asteroide choca amb la nau finalitza joc.
            for (Grafic asteroide : asteroides) {
                if (asteroide.verificaColisio(nau)) {
                    estat = ESTAT_DERROTA;
                    sortir();
                }
            }
        }
    }

    //MÈTODES AUXILIARS
    private void destrueixAsteroide(int i) {
        int tam;
        synchronized (asteroides) {
            if (asteroides.get(i).getDrawable()!=drawableAsteroide[2]){
                if (asteroides.get(i).getDrawable()==drawableAsteroide[1]){
                    tam=2; //[1]
                }else{ //[0]
                    tam=1;
                }
                for (int n=0;n<numFragmentos;n++){
                    Grafic asteroide = new Grafic(this,drawableAsteroide[tam]);
                    asteroide.setCenX(asteroides.get(i).getCenX());
                    asteroide.setCenY(asteroides.get(i).getCenY());
                    asteroide.setIncY(Math.random()*7-2-tam);
                    asteroide.setIncX(Math.random()*7-2-tam);
                    asteroide.setAngle((int)(Math.random()*360));
                    asteroide.setRotacio((int)(Math.random()*8-4));
                    asteroides.add(asteroide);
                }
            }
            asteroides.remove(i);
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (pref.getBoolean("musica1",true)) {
            soundPool.play(idExplosio, 1, 1, 1, 0, 1);
        }
        puntuacio+=1000;
        //Si no queda cap asteroides en pantalla finalitza partida
        if (asteroides.isEmpty()){
            //Actualitza l'estat de la partida a Victoria
            estat=ESTAT_VICTORIA;
            //Si no queda cap asteroides en pantalla finalitza partida
            sortir();
        }
    }

    private void activaMisil(Grafic missil){
        if (estat==ESTAT_JUGANT) {
            missil.setCenX(nau.getCenX());
            missil.setCenY(nau.getCenY());
            missil.setAngle(nau.getAngle());
            missil.setIncX(Math.cos(Math.toRadians(missil.getAngle())) * PAS_VELOCITAT_MISSIL);
            missil.setIncY(Math.sin(Math.toRadians(missil.getAngle())) * PAS_VELOCITAT_MISSIL);
            int tmpMissil = (int) Math.min(
                    this.getWidth() / Math.abs(missil.getIncX()),
                    this.getHeight() / Math.abs(missil.getIncY())) - 2;
            missils.add(missil);
            tempsMissil.add(tmpMissil);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            if (pref.getBoolean("musica1", true)) {
                soundPool.play(idDispar, 1, 1, 1, 0, 1);
            }
        }
    }

    @Override
    //GESTIÓ D'EVENTS
        public boolean onKeyDown(int codiTecla, KeyEvent event){
        super.onKeyDown(codiTecla,event);
        boolean processada = false;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (pref.getString("entrada","1").equals("0")) {
            //Suposem que processem la pulsació
            processada = true;
            switch (codiTecla) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    acceleracioNau = +PAS_ACCELERACIO_NAU;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    girNau = -PAS_GIR_NAU;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    girNau = +PAS_GIR_NAU;
                    break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    activaMisil(new Grafic(this, drawableMissil));
                    break;
                default:
                    //Si arriba aqui, no hi ha pulsació que interessi
                    processada = false;
                    break;
            }
        }
        return processada; //Hem processat l'event
    }

    @Override
    public boolean onKeyUp(int codiTecla, KeyEvent event){
        super.onKeyDown(codiTecla,event);
        boolean processada = false;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (pref.getString("entrada","1").equals("0")) {
            processada = true;
            switch (codiTecla) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    acceleracioNau = 0;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    girNau = 0;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    girNau = 0;
                    break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    activaMisil(new Grafic(this, drawableMissil));
                    break;
                default:
                    processada = false;
                    break;
            }
        }
        return processada;
    }

    //Manejador d'events de la pantalla tàctil per la nau
    private float mX=0,mY=0;
    private boolean dispar=false;

    //GESTIÓ D'EVENTS DE LA NAU AMB PANTALLA TACTIL
    @Override
    public boolean onTouchEvent(MotionEvent mevent){
        super.onTouchEvent(mevent);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        //Si el tipus d'entrada és amb la pantalla tàctil
        if (pref.getString("entrada","1").equals("1")) {
            float x = mevent.getX();
            float y = mevent.getY();
            switch (mevent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dispar = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = Math.abs(x - mX);
                    float dy = Math.abs(y - mY);
                    if (dy < 6 && dx > 6) {
                        girNau = Math.round((x - mX) / 2);
                        dispar = false;
                    } else if (dx < 6 && dy > 6) {
                        acceleracioNau = Math.round((mY - y) / 25);
                        dispar = false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    girNau = 0;
                    acceleracioNau = 0;
                    break;
            }
            mX = x;
            mY = y;
        }
        //Aquestes accions s'han de fer sempre ja que per disparar un missil sempre es farà a través de la pantalla tàctil
        switch (mevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dispar = true;
                break;
            case MotionEvent.ACTION_UP:
                if (dispar){
                    activaMisil(new Grafic(this, drawableMissil));
                }
                break;
        }
        return true;
    }

    //GESTIÓ D'EVENTS DE SENSORS PER LA NAU
    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){
    }

    private boolean hihaValorInicial=false;
    private float valorInicial;

    @Override
    public void onSensorChanged(SensorEvent event){
        float valor=event.values[1]; //eix Y
        if (!hihaValorInicial){
            valorInicial=0;
            hihaValorInicial=true;
        }
        girNau=(int)(valor-valorInicial);
    }

    //Mètode que permet finalitzar el joc retornant la puntuació
    private void sortir(){
        Bundle bundle=new Bundle();
        bundle.putInt("puntuacio",puntuacio);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        pare.setResult(Activity.RESULT_OK,intent);
    }

    //CLASSE QUE CREA UN NOU FIL
    class ThreadJoc extends Thread{
        private boolean pausa,corrent;

        public synchronized void pausar(){
            pausa=true;
        }
        public synchronized void reanudar(){
            pausa=false;
            notify();
        }
        public synchronized void aturar(){
            corrent=false;
            if (pausa)reanudar();
        }
        public void run(){
            corrent=true;
            while (corrent){
                actualitzaFisica();
                synchronized (this){
                    while (pausa){
                        try {
                            wait();
                        }catch (Exception e){
                        }
                    }
                }
            }
        }
    }

    public ThreadJoc getFil() {
        return fil;
    }
}
