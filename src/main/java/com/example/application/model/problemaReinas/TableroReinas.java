package com.example.application.model.problemaReinas;

import java.util.ArrayList;
import java.util.List;

public class TableroReinas {
    private int tamaño;
    private List<PiezaReinas> reinas;
    private int[][] tablero; // Matriz para representar el tablero

    public TableroReinas(int tamaño) {
        this.tamaño = tamaño;
        this.reinas = new ArrayList<>();
        this.tablero = new int[tamaño][tamaño];
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                tablero[i][j] = 0; // 0 representa casilla vacía
            }
        }
    }

    public boolean agregarReina(PiezaReinas reina) {
        if (esPosicionSegura(reina.getFila(), reina.getColumna())) {
            reinas.add(reina);
            tablero[reina.getFila()][reina.getColumna()] = 1; // 1 representa reina
            return true;
        }
        return false;
    }

    public void colocarReinaInicial(int fila, int columna) {
        reinas.add(new PiezaReinas(fila, columna));
        tablero[fila][columna] = 1;
    }

    public void eliminarReina(int fila, int columna) {
        reinas.removeIf(r -> r.getFila() == fila && r.getColumna() == columna);
        tablero[fila][columna] = 0;
    }

    public boolean esPosicionSegura(int fila, int columna) {
        if (fila < 0 || fila >= tamaño || columna < 0 || columna >= tamaño) {
            return false;
        }

        PiezaReinas nuevaReina = new PiezaReinas(fila, columna);
        for (PiezaReinas reina : reinas) {
            if (reina.amenaza(nuevaReina)) {
                return false;
            }
        }
        return true;
    }

    public boolean solucionCompleta() {
        return reinas.size() == tamaño && todasSeguras();
    }

    private boolean todasSeguras() {
        for (int i = 0; i < reinas.size(); i++) {
            for (int j = i + 1; j < reinas.size(); j++) {
                if (reinas.get(i).amenaza(reinas.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<PiezaReinas> getReinas() {
        return new ArrayList<>(reinas);
    }

    public int getTamaño() {
        return tamaño;
    }

    public void reiniciar() {
        reinas.clear();
        inicializarTablero();
    }

    public int[][] getEstadoTablero() {
        return tablero.clone();
    }

    public boolean tieneReina(int fila, int columna) {
        return tablero[fila][columna] == 1;
    }
}