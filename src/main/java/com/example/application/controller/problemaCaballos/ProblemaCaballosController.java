package com.example.application.controller.problemaCaballos;

import com.example.application.model.problemaCaballos.PiezaCaballo;
import com.example.application.model.problemaCaballos.TableroCaballos;
import java.util.List;

public class ProblemaCaballosController {
    private TableroCaballos tablero;
    private int intentos;

    public ProblemaCaballosController(int tamaño) {
        this.tablero = new TableroCaballos(tamaño);
        this.intentos = 0;
    }

    public boolean agregarMovimiento(int fila, int columna) {
        intentos++;
        return tablero.agregarMovimiento(fila, columna);
    }

    public List<PiezaCaballo> getMovimientos() {
        return tablero.getMovimientos();
    }

    public int getTamaño() {
        return tablero.getTamaño();
    }

    public boolean solucionCompleta() {
        return tablero.solucionCompleta();
    }

    public void reiniciar() {
        tablero.reiniciar();
        intentos = 0;
    }

    public int[][] getEstadoTablero() {
        return tablero.getEstadoTablero();
    }

    public boolean resolverAutomatico(int filaInicial, int columnaInicial) {
        reiniciar();
        return tablero.resolverAutomatico(filaInicial, columnaInicial);
    }

    public int getNumeroIntentos() {
        return intentos;
    }
}