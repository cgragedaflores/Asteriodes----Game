package com.example.GragedaF_ex12_1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class MagatzemPuntuacionsSQLiteRelacional extends SQLiteOpenHelper implements MagatzemPuntuacions {
    public MagatzemPuntuacionsSQLiteRelacional(Context context){
        super(context,"puntuacions",null,2);
    }

    @Override
    //Aquest mètode només es cridarà una vegada, quan el sistema detecti que la base de dades
    //encara no està creada.
    public void onCreate(SQLiteDatabase db) {
        //Aquí s'han de crear totes les taules de la BD, i inicialitzar les dades si és necessari.
        //CREATE TABLE nom_taula(nom_columna tipus [atributs], ...)
        db.execSQL("CREATE TABLE usuaris (" + "usu_id INTEGER PRIMARY " +
                "KEY AUTOINCREMENT," + "nom TEXT, correu TEXT)");
        db.execSQL("CREATE TABLE vpuntuacions2 (" + "punts_id INTEGER PRIMARY " +
                "KEY AUTOINCREMENT," + "punts INTEGER, data DATE, usuari INTEGER," +
                "FOREIGN KEY (usuari) REFERENCES usuaris (usu_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if (oldVersion<2){
            db.execSQL("CREATE TABLE usuaris (" + "usu_id INTEGER PRIMARY " +
                    "KEY AUTOINCREMENT," + "nom TEXT, correu TEXT)");
            db.execSQL("CREATE TABLE vpuntuacions2 (" + "punts_id INTEGER PRIMARY " +
                    "KEY AUTOINCREMENT," + "punts INTEGER, data DATE, usuari INTEGER," +
                    "FOREIGN KEY (usuari) REFERENCES usuaris (usu_id))");
            Cursor cursor=db.rawQuery("SELECT punts, nom, data FROM "+
                    "vpuntuacions ORDER BY punts ",null);
            Cursor cursor1=null;
            while (cursor.moveToNext()){
                cursor1=db.rawQuery("SELECT usu_id FROM usuaris WHERE nom='"+cursor.getString(1)+"'",null);
                if (cursor1.moveToNext()){
                    db.execSQL("INSERT INTO vpuntuacions2 VALUES (null, "+cursor.getInt(0)+","+
                            " '"+cursor.getString(2)+"', "+cursor1.getInt(0)+")");
                }else {
                    db.execSQL("INSERT INTO usuaris VALUES (null, '"+cursor.getString(1)+"', '"+ cursor.getString(1)+"@gmail.com')");
                }
            }
            cursor.close();
            cursor1.close();
            //db.close();
        //}
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        //Obté una referència a la nostra BD en mode L/E.
        SQLiteDatabase db = getWritableDatabase();
        //Sentència SQL que afegeix una fila a la taula.
        //INSERT INTO nom_taula VALUES (valor1, valor2, ...)
        //Els valors cadena van entre cometes
        Cursor cursor=db.rawQuery("SELECT usu_id FROM usuaris WHERE nom='"+nom+"'",null);
        if (cursor.moveToNext()){
            db.execSQL("INSERT INTO vpuntuacions2 VALUES (null, "+punts+","+
                    " '"+data+"', "+cursor.getInt(0)+")");
        }else {
            db.execSQL("INSERT INTO usuaris VALUES (null, '"+nom+"', '"+ nom+"@gmail.com')");
        }
        cursor.close();
        db.close();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<String>();
        //Obté una referència a la nostra BD en mode lectura.
        SQLiteDatabase db = getReadableDatabase();
        //Obté un cursor que conté totes les files de la consulta.
        //SELECT columna1, ... FROM nom_taula ORDER BY columna [mode]
        //LIMIT numero
        Cursor cursor=db.rawQuery("SELECT punts, data, usuari FROM "+
                "vpuntuacions2 ORDER BY punts DESC LIMIT "+quantitat,null);
        Cursor cursor1=null;
        while (cursor.moveToNext()){
            cursor1=db.rawQuery("SELECT nom, correu FROM "+
                    "usuaris WHERE usu_id="+cursor.getInt(2),null);
            cursor1.moveToFirst();
            result.add(cursor.getInt(0)+" "+cursor1.getString(0)+" "+
                    cursor.getString(1)+" "+cursor1.getString(1));
        }
        cursor.close();
        cursor1.close();
        db.close();
        return result;
    }
}
