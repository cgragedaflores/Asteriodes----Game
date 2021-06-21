package com.example.GragedaF_ex12_1;

import java.util.Vector;

public interface MagatzemPuntuacions {
    public void guardarPuntuacio(int punts, String nom, long data);
    public Vector<String> llistaPuntuacions(int quantitat);
}
