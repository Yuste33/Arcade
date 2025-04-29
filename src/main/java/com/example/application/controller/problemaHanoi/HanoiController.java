package com.example.application.controller.problemaHanoi;

import com.example.application.model.problemaHanoi.JuegoHanoi;
import com.example.application.model.problemaHanoi.Torre;

import java.util.List;

public class HanoiController {
    private JuegoHanoi juego;

    public HanoiController(int numDiscos) {
        this.juego = new JuegoHanoi(numDiscos);
    }

    public boolean moverDisco(Torre origen, Torre destino) {
        return juego.moverDisco(origen, destino);
    }

    public void resolverAutomatico() {
        juego.resolverAutomatico();
    }

    public void reiniciar() {
        juego.reiniciar();
    }

    public boolean juegoCompletado() {
        return juego.juegoCompletado();
    }

    // Getters
    public JuegoHanoi getJuego() { return juego; }
    public int getMovimientos() { return juego.getMovimientos(); }
    public int getNumDiscos() { return juego.getNumDiscos(); }
    public List<String> getHistorial() { return juego.getHistorial(); }
}