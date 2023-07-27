package com.example.proyectorestaurante.models;

public class Personal {
    private int id;
    private String nombre;
    private String apellido;
    private String cargo;
    private String dni;

    public Personal(int id, String nombre, String apellido, String cargo, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cargo = cargo;
        this.dni = dni;
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

    public String getDni() {
        return dni;
    }
}
