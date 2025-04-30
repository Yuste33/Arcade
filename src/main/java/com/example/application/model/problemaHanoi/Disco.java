package com.example.application.model.problemaHanoi;

public class Disco {
    private final int tamaño;
    private final String color;

    public Disco(int tamaño, String color) {
        this.tamaño = tamaño;
        this.color = color;
    }

    public int getTamaño() {
        return tamaño;
    }

    public String getColor() {
        return color;
    }
}