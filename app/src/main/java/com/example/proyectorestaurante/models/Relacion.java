package com.example.proyectorestaurante.models;

public class Relacion {
    private int cRelacion;
    private String nDescripcion;

    public Relacion(int cRelacion, String nDescripcion) {
        this.cRelacion = cRelacion;
        this.nDescripcion = nDescripcion;
    }

    public int getcRelacion() {
        return cRelacion;
    }

    public void setcRelacion(int cRelacion) {
        this.cRelacion = cRelacion;
    }

    public String getnDescripcion() {
        return nDescripcion;
    }

    public void setnDescripcion(String nDescripcion) {
        this.nDescripcion = nDescripcion;
    }
}
