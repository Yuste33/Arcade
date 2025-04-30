package com.example.application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "partidas_caballos")
public class PartidaCaballos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tamañoTablero;  // Tamaño NxN del tablero (ej: 8)
    private boolean resuelto;    // ¿Se completó el recorrido?
    private int movimientos;     // Casillas visitadas (ej: 64 en un 8x8 resuelto)

    // Constructor, getters y setters
    public PartidaCaballos() {}

    // Getters y setters (igual que en PartidaReinas)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTamañoTablero() {
        return tamañoTablero;
    }

    public void setTamañoTablero(int tamañoTablero) {
        this.tamañoTablero = tamañoTablero;
    }

    public boolean isResuelto() {
        return resuelto;
    }

    public void setResuelto(boolean resuelto) {
        this.resuelto = resuelto;
    }

    public int getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(int movimientos) {
        this.movimientos = movimientos;
    }
}