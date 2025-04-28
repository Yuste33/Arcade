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
}