package com.example.proyectorestaurante.models;

import java.util.Date;

public class Foro {
    private int id_foro;
    private String nombre_foro;
    private String descripcion;
    private Date fecha_creacion;

    // Constructor


    public Foro(int id_foro, String nombre_foro, String descripcion, Date fecha_creacion) {
        this.id_foro = id_foro;
        this.nombre_foro = nombre_foro;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
    }

    public int getId() {
        return id_foro;
    }

    public void setId(int id_foro) {
        this.id_foro = id_foro;
    }

    public String getNombre_foro() {
        return nombre_foro;
    }

    public void setNombre_foro(String nombre_foro) {
        this.nombre_foro = nombre_foro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    @Override
    public String toString() {
        return "Foro{" +
                "id_foro=" + id_foro +
                ", nombreForo='" + nombre_foro + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}

