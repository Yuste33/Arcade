package com.example.application.model.problemaHanoi;

import java.util.Stack;

public class Torre {
    private final Stack<Disco> discos;
    private final String nombre;

    public Torre(String nombre) {
        this.nombre = nombre;
        this.discos = new Stack<>();
    }

    public void apilar(Disco disco) {
        discos.push(disco);
    }

    public Disco desapilar() {
        return discos.pop();
    }

    public boolean estaVacia() {
        return discos.isEmpty();
    }

    public Disco cima() {
        return discos.peek();
    }

    public Stack<Disco> getDiscos() {
        return discos;
    }

    public String getNombre() {
        return nombre;
    }
}