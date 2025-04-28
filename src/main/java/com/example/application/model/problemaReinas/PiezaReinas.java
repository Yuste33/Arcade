package com.example.application.model.problemaReinas;

public class PiezaReinas {
    private int fila;
    private int columna;

    public PiezaReinas(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public boolean amenaza(PiezaReinas otra) {
        return this.fila == otra.fila ||
                this.columna == otra.columna ||
                Math.abs(this.fila - otra.fila) == Math.abs(this.columna - otra.columna);
    }
}