package com.example.proyectorestaurante.recycler;

public class Players {
    private int id;
    private String nombre;
    private String apellido;
    private String cargo;

    public Players(int id, String nombre, String apellido, String cargo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cargo = cargo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCargo() {
        return cargo;
    }
}
