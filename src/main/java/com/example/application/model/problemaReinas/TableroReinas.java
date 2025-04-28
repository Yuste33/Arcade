package com.example.application.model.problemaReinas;

import java.util.ArrayList;
import java.util.List;

public class TableroReinas {
    private int tamaño;
    private List<PiezaReinas> reinas;

    public TableroReinas(int tamaño) {
        this.tamaño = tamaño;
        this.reinas = new ArrayList<>();
    }

    public boolean agregarReina(PiezaReinas reina) {
        if (esPosicionSegura(reina.getFila(), reina.getColumna())) {
            reinas.add(reina);
            return true;
        }
        return false;
    }

    public boolean esPosicionSegura(int fila, int columna) {
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
    }
}