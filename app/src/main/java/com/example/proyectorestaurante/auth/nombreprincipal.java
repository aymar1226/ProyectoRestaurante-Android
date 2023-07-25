package com.example.proyectorestaurante.auth;

import com.example.proyectorestaurante.utils.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class nombreprincipal {
    public nombreprincipal(){}
    public String obtenerNombreUsuario(int id) {
        String nombre = null;

        try {
            // Establecer la conexión a la base de datos SQL Server
            Connection connection = ConexionDB.obtenerConexion();

            // Ejecutar la consulta SQL para obtener el nombre del usuario según su ID
            String sql = "SELECT nombre FROM usuarios WHERE id_usuario = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Obtener el nombre del usuario si se encuentra
            if (resultSet.next()) {
                nombre = resultSet.getString("nombre");
            }

            // Cerrar los recursos
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nombre;
    }
}
