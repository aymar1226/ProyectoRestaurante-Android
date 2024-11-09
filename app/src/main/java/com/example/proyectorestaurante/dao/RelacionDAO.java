package com.example.proyectorestaurante.dao;

import com.example.proyectorestaurante.models.Relacion;

import java.util.List;

public interface RelacionDAO {
    List<Relacion> getAllRelaciones();
    Relacion getRelacionById(int cRelacion);
    void insertRelacion(Relacion relacion);
    void updateRelacion(Relacion relacion);
    void deleteRelacion(int cRelacion);
}