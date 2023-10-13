package com.example.proyectorestaurante.models;

public class Platos {

    private int id;
    private String nombre;
    private double precio;
    private String imagen_ruta;
    private String categoria;
    private String descripcion;
    private String imagePath;

    public Platos(int id, String nombre, double precio, String imagen_ruta, String categoria, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen_ruta = imagen_ruta;
        this.categoria = categoria;
        this.descripcion = descripcion;
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

    public String getImagen_ruta() {
        return imagen_ruta;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
