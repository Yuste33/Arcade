package com.example.application.controller.problemaReinas;

import com.example.application.model.problemaReinas.PiezaReinas;
import com.example.application.model.problemaReinas.TableroReinas;

import java.util.List;

public class ProblemaReinasController {
    private TableroReinas tablero;
    private int intentos;

    public ProblemaReinasController(int tamaño) {
        this.tablero = new TableroReinas(tamaño);
        this.intentos = 0;
    }

    public boolean agregarReina(PiezaReinas reina) {
        intentos++;
        return tablero.agregarReina(reina);
    }

    public boolean esSeguro(int fila, int columna) {
        return tablero.esPosicionSegura(fila, columna);
    }

    public List<PiezaReinas> getSolucion() {
        return tablero.getReinas();
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

    public boolean resolverDesdePosicion(int filaInicial, int columnaInicial) {
        if (filaInicial < 0 || filaInicial >= tablero.getTamaño() ||
                columnaInicial < 0 || columnaInicial >= tablero.getTamaño()) {
            return false;
        }

        reiniciar(); // Reinicia tablero e intentos
        tablero.colocarReinaInicial(filaInicial, columnaInicial);
        intentos++; // Contar la primera colocación

        return resolverRecursivo(columnaInicial + 1);
    }

    private boolean resolverRecursivo(int columna) {
        if (columna >= tablero.getTamaño()) {
            return true;
        }

        for (int fila = 0; fila < tablero.getTamaño(); fila++) {
            if (tablero.esPosicionSegura(fila, columna)) {
                tablero.agregarReina(new PiezaReinas(fila, columna));
                intentos++;

                if (resolverRecursivo(columna + 1)) {
                    return true;
                }

                tablero.eliminarReina(fila, columna);
            }
        }

        return false;
    }

    public int[][] getEstadoTablero() {
        return tablero.getEstadoTablero();
    }

    public boolean tieneReina(int fila, int columna) {
        return tablero.tieneReina(fila, columna);
    }

    public int getNumeroIntentos() {
        return intentos;
    }
}
