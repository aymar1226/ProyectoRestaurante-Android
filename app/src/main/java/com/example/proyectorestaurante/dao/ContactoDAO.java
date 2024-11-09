package com.example.proyectorestaurante.dao;

import com.example.proyectorestaurante.models.Contacto;

import java.util.List;

public interface ContactoDAO {
    List<Contacto> getAllContactos();
    Contacto getContactoById(int cContacto);
    void insertContacto(Contacto contacto);
    void updateContacto(Contacto contacto);
    void deleteContacto(int cContacto);
}
