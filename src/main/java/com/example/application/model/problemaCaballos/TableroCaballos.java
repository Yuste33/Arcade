package com.example.application.model.problemaCaballos;

import java.util.ArrayList;
import java.util.List;

public class TableroCaballos {
    private int tamaño;
    private List<PiezaCaballo> movimientos;
    private int[][] tablero;

    public TableroCaballos(int tamaño) {
        this.tamaño = tamaño;
        this.movimientos = new ArrayList<>();
        this.tablero = new int[tamaño][tamaño];
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                tablero[i][j] = -1;
            }
        }
    }

    public boolean agregarMovimiento(int fila, int columna) {
        if (fila < 0 || fila >= tamaño || columna < 0 || columna >= tamaño || tablero[fila][columna] != -1) {
            return false;
        }

        if (movimientos.isEmpty() || movimientos.get(movimientos.size() - 1).esMovimientoValido(fila, columna)) {
            int numeroMovimiento = movimientos.size();
            tablero[fila][columna] = numeroMovimiento;
            movimientos.add(new PiezaCaballo(fila, columna, numeroMovimiento));
            return true;
        }
        return false;
    }

    public boolean solucionCompleta() {
        return movimientos.size() == tamaño * tamaño;
    }

    public List<PiezaCaballo> getMovimientos() {
        return new ArrayList<>(movimientos);
    }

    public int getTamaño() {
        return tamaño;
    }

    public void reiniciar() {
        movimientos.clear();
        inicializarTablero();
    }

    public int[][] getEstadoTablero() {
        return tablero.clone();
    }


    public boolean resolverAutomatico(int filaInicial, int columnaInicial) {
        reiniciar();
        if (filaInicial < 0 || filaInicial >= tamaño || columnaInicial < 0 || columnaInicial >= tamaño) {
            return false;
        }

        int[] movX = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] movY = {1, 2, 2, 1, -1, -2, -2, -1};

        int x = filaInicial;
        int y = columnaInicial;
        tablero[x][y] = 0;
        movimientos.add(new PiezaCaballo(x, y, 0));

        for (int i = 1; i < tamaño * tamaño; i++) {
            int nextMove = encontrarSiguienteMovimiento(x, y, movX, movY);
            if (nextMove == -1) {
                return false;
            }

            x += movX[nextMove];
            y += movY[nextMove];
            tablero[x][y] = i;
            movimientos.add(new PiezaCaballo(x, y, i));
        }

        return true;
    }

    private int encontrarSiguienteMovimiento(int x, int y, int[] movX, int[] movY) {
        int minDegreeIndex = -1;
        int minDegree = 9; // Más que el máximo posible (8)

        for (int i = 0; i < 8; i++) {
            int nextX = x + movX[i];
            int nextY = y + movY[i];

            if (esMovimientoValido(nextX, nextY) && tablero[nextX][nextY] == -1) {
                int degree = calcularGrado(nextX, nextY);
                if (degree < minDegree) {
                    minDegree = degree;
                    minDegreeIndex = i;
                }
            }
        }

        return minDegreeIndex;
    }

    private int calcularGrado(int x, int y) {
        int count = 0;
        int[] movX = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] movY = {1, 2, 2, 1, -1, -2, -2, -1};

        for (int i = 0; i < 8; i++) {
            int nextX = x + movX[i];
            int nextY = y + movY[i];
            if (esMovimientoValido(nextX, nextY) && tablero[nextX][nextY] == -1) {
                count++;
            }
        }

        return count;
    }

    private boolean esMovimientoValido(int x, int y) {
        return (x >= 0 && x < tamaño && y >= 0 && y < tamaño);
    }
}