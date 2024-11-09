package com.example.proyectorestaurante.dao;

import com.example.proyectorestaurante.models.Foro;
import com.example.proyectorestaurante.utils.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForoDao {
    public boolean crearForo(String nombreForo, String descripcion) {
        String sql = "INSERT INTO Foros (nombre_foro, descripcion) VALUES (?, ?)";
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreForo);
            stmt.setString(2, descripcion);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Foro> obtenerForos() {
        String sql = "SELECT * FROM Foros";
        List<Foro> listaForos = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_foro");
                String nombre_foro = rs.getString("nombre_foro");
                String descripcion = rs.getString("descripcion");
                Date fecha_creacion = rs.getDate("fecha_creacion");

                Foro foro = new Foro(id, nombre_foro, descripcion,fecha_creacion);
                listaForos.add(foro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaForos;
    }

}
