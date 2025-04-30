package com.example.application.controller.problemaHanoi;

import com.example.application.model.problemaHanoi.JuegoHanoi;
import com.example.application.model.problemaHanoi.Torre;

import java.util.List;

public class HanoiController {
    private JuegoHanoi juego;
    private int movimientos;

    public HanoiController(int numDiscos) {
        this.juego = new JuegoHanoi(numDiscos);
        this.movimientos = 0; // Asegurarnos que se inicializa
    }

    public boolean moverDisco(Torre origen, Torre destino) {
        boolean movimientoValido = juego.moverDisco(origen, destino);
        if (movimientoValido) {
            movimientos++; // Incrementar contador
        }
        return movimientoValido;
    }

    public int getMovimientos() {
        return movimientos;
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
    public int getNumDiscos() { return juego.getNumDiscos(); }
    public List<String> getHistorial() { return juego.getHistorial(); }
}