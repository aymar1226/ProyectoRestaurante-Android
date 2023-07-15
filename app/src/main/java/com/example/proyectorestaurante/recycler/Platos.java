package com.example.proyectorestaurante.recycler;

public class Platos {

    private int id;
    private String nombre;
    private double precio;
    private String imagen;
    private String categoria;

    public Platos(int id, String nombre, double precio, String imagen, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

    public String getCategoria() {
        return categoria;
    }
}
