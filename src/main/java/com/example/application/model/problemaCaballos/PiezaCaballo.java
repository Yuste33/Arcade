package com.example.application.model.problemaCaballos;

public class PiezaCaballo {
    private int fila;
    private int columna;
    private int numeroMovimiento;

    public PiezaCaballo(int fila, int columna, int numeroMovimiento) {
        this.fila = fila;
        this.columna = columna;
        this.numeroMovimiento = numeroMovimiento;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getNumeroMovimiento() {
        return numeroMovimiento;
    }

    public boolean esMovimientoValido(int nuevaFila, int nuevaColumna) {
        int diffFila = Math.abs(nuevaFila - fila);
        int diffColumna = Math.abs(nuevaColumna - columna);
        return (diffFila == 2 && diffColumna == 1) || (diffFila == 1 && diffColumna == 2);
    }
}