package com.example.application.controller.problemaCaballos;

import com.example.application.model.problemaCaballos.PiezaCaballo;
import com.example.application.model.problemaCaballos.TableroCaballos;
import java.util.List;

public class ProblemaCaballosController {
    private TableroCaballos tablero;

    public ProblemaCaballosController(int tamaño) {
        this.tablero = new TableroCaballos(tamaño);
    }

    public boolean agregarMovimiento(int fila, int columna) {
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
    }

    public int[][] getEstadoTablero() {
        return tablero.getEstadoTablero();
    }


    public boolean resolverAutomatico(int filaInicial, int columnaInicial) {
        return tablero.resolverAutomatico(filaInicial, columnaInicial);
    }
}