package com.example.proyectorestaurante.models;

public class Contacto {
    private int cContacto;
    private String nNombre;
    private String nNumero;
    private int cRelacion; // Clave foránea a `Relacion`

    public Contacto(int cContacto, String nNombre, String nNumero, int cRelacion) {
        this.cContacto = cContacto;
        this.nNombre = nNombre;
        this.nNumero = nNumero;
        this.cRelacion = cRelacion;
    }

    public int getcContacto() {
        return cContacto;
    }

    public void setcContacto(int cContacto) {
        this.cContacto = cContacto;
    }

    public String getnNombre() {
        return nNombre;
    }

    public void setnNombre(String nNombre) {
        this.nNombre = nNombre;
    }

    public String getnNumero() {
        return nNumero;
    }

    public void setnNumero(String nNumero) {
        this.nNumero = nNumero;
    }

    public int getcRelacion() {
        return cRelacion;
    }

    public void setcRelacion(int cRelacion) {
        this.cRelacion = cRelacion;
    }
}
