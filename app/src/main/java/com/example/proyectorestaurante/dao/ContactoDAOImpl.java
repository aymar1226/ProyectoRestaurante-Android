package com.example.proyectorestaurante.dao;



import com.example.proyectorestaurante.models.Contacto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ContactoDAOImpl implements ContactoDAO {
    private Connection connection;

    public ContactoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Contacto> getAllContactos() {
        List<Contacto> contactos = new ArrayList<>();
        String sql = "SELECT * FROM contacto";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                contactos.add(new Contacto(
                        resultSet.getInt("cContacto"),
                        resultSet.getString("nNombre"),
                        resultSet.getString("nNumero"),
                        resultSet.getInt("cRelacion")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactos;
    }

    @Override
    public Contacto getContactoById(int cContacto) {
        Contacto contacto = null;
        String sql = "SELECT * FROM contacto WHERE cContacto = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cContacto);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                contacto = new Contacto(
                        resultSet.getInt("cContacto"),
                        resultSet.getString("nNombre"),
                        resultSet.getString("nNumero"),
                        resultSet.getInt("cRelacion")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacto;
    }

    @Override
    public void insertContacto(Contacto contacto) {
        String sql = "INSERT INTO contacto (nNombre, nNumero, cRelacion) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, contacto.getnNombre());
            statement.setString(2, contacto.getnNumero());
            statement.setInt(3, contacto.getcRelacion());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateContacto(Contacto contacto) {
        String sql = "UPDATE contacto SET nNombre = ?, nNumero = ?, cRelacion = ? WHERE cContacto = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, contacto.getnNombre());
            statement.setString(2, contacto.getnNumero());
            statement.setInt(3, contacto.getcRelacion());
            statement.setInt(4, contacto.getcContacto());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteContacto(int cContacto) {
        String sql = "DELETE FROM contacto WHERE cContacto = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cContacto);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
