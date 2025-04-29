package com.example.application.model.problemaHanoi;

import java.util.ArrayList;
import java.util.List;

public class JuegoHanoi {
    private final Torre torreA;
    private final Torre torreB;
    private final Torre torreC;
    private int movimientos;
    private final int numDiscos;
    private final List<String> historial;

    public JuegoHanoi(int numDiscos) {
        this.numDiscos = numDiscos;
        this.torreA = new Torre("Torre A");
        this.torreB = new Torre("Torre B");
        this.torreC = new Torre("Torre C");
        this.movimientos = 0;
        this.historial = new ArrayList<>();

        inicializarTorres();
    }

    private void inicializarTorres() {
        // Colores para los discos
        String[] colores = {"#FF0000", "#FF7F00", "#FFFF00", "#00FF00", "#0000FF", "#4B0082", "#9400D3"};

        for (int i = numDiscos; i > 0; i--) {
            torreA.apilar(new Disco(i, colores[(i-1) % colores.length]));
        }
    }

    public boolean moverDisco(Torre origen, Torre destino) {
        if (origen.estaVacia()) {
            return false;
        }

        if (!destino.estaVacia() && origen.cima().getTamaño() > destino.cima().getTamaño()) {
            return false;
        }

        Disco disco = origen.desapilar();
        destino.apilar(disco);
        movimientos++;

        historial.add("Movimiento " + movimientos + ": " + origen.getNombre() + " -> " + destino.getNombre());
        return true;
    }

    public boolean juegoCompletado() {
        return torreC.getDiscos().size() == numDiscos;
    }

    public void resolverAutomatico() {
        reiniciar();
        moverDiscos(numDiscos, torreA, torreC, torreB);
    }

    private void moverDiscos(int n, Torre origen, Torre destino, Torre auxiliar) {
        if (n > 0) {
            moverDiscos(n - 1, origen, auxiliar, destino);
            moverDisco(origen, destino);
            moverDiscos(n - 1, auxiliar, destino, origen);
        }
    }

    public void reiniciar() {
        torreA.getDiscos().clear();
        torreB.getDiscos().clear();
        torreC.getDiscos().clear();
        movimientos = 0;
        historial.clear();
        inicializarTorres();
    }

    // Getters
    public Torre getTorreA() { return torreA; }
    public Torre getTorreB() { return torreB; }
    public Torre getTorreC() { return torreC; }
    public int getMovimientos() { return movimientos; }
    public int getNumDiscos() { return numDiscos; }
    public List<String> getHistorial() { return historial; }
}