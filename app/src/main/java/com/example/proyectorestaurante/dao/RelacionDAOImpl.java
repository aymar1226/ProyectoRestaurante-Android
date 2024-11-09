package com.example.proyectorestaurante.dao;

import com.example.proyectorestaurante.models.Relacion;
import com.example.proyectorestaurante.utils.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RelacionDAOImpl implements RelacionDAO {
    private Connection connection;

    public RelacionDAOImpl() {
        this.connection = ConexionDB.obtenerConexion();
    }

    @Override
    public List<Relacion> getAllRelaciones() {
        List<Relacion> relaciones = new ArrayList<>();
        String sql = "SELECT * FROM relacion";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                relaciones.add(new Relacion(
                        resultSet.getInt("cRelacion"),
                        resultSet.getString("nDescripcion")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return relaciones;
    }

    @Override
    public Relacion getRelacionById(int cRelacion) {
        Relacion relacion = null;
        String sql = "SELECT * FROM relacion WHERE cRelacion = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cRelacion);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                relacion = new Relacion(
                        resultSet.getInt("cRelacion"),
                        resultSet.getString("nDescripcion")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return relacion;
    }

    @Override
    public void insertRelacion(Relacion relacion) {
        String sql = "INSERT INTO relacion (nDescripcion) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, relacion.getnDescripcion());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRelacion(Relacion relacion) {
        String sql = "UPDATE relacion SET nDescripcion = ? WHERE cRelacion = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, relacion.getnDescripcion());
            statement.setInt(2, relacion.getcRelacion());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRelacion(int cRelacion) {
        String sql = "DELETE FROM relacion WHERE cRelacion = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cRelacion);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}