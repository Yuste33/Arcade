package com.example.application.controller.problemaReinas;

import com.example.application.model.problemaReinas.PiezaReinas;
import com.example.application.model.problemaReinas.TableroReinas;
import java.util.List;

public class ProblemaReinasController {
    private TableroReinas tablero;

    public ProblemaReinasController(int tamaño) {
        this.tablero = new TableroReinas(tamaño);
    }

    public boolean agregarReina(PiezaReinas reina) {
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
    }

    public boolean resolverDesdePosicion(int filaInicial, int columnaInicial) {
        if (filaInicial < 0 || filaInicial >= tablero.getTamaño() ||
                columnaInicial < 0 || columnaInicial >= tablero.getTamaño()) {
            return false;
        }


        tablero.reiniciar();
        tablero.colocarReinaInicial(filaInicial, columnaInicial);

        return resolverRecursivo(columnaInicial + 1);
    }

    private boolean resolverRecursivo(int columna) {
        if (columna >= tablero.getTamaño()) {
            return true;
        }

        for (int fila = 0; fila < tablero.getTamaño(); fila++) {
            if (tablero.esPosicionSegura(fila, columna)) {
                tablero.agregarReina(new PiezaReinas(fila, columna));

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
}