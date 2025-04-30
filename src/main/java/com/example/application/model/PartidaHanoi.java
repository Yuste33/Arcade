package com.example.application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "partidas_hanoi")
public class PartidaHanoi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numDiscos;
    private boolean resuelto;
    private int movimientos;

    // Constructor, getters y setters
    public PartidaHanoi() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getNumDiscos() { return numDiscos; }
    public void setNumDiscos(int numDiscos) { this.numDiscos = numDiscos; }

    public boolean isResuelto() { return resuelto; }
    public void setResuelto(boolean resuelto) { this.resuelto = resuelto; }

    public int getMovimientos() { return movimientos; }
    public void setMovimientos(int movimientos) { this.movimientos = movimientos; }
}