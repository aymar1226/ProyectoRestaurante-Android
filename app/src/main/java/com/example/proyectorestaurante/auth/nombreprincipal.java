package com.example.proyectorestaurante.auth;

import android.content.Context;

import com.example.proyectorestaurante.utils.ConexionDB;
import com.example.proyectorestaurante.utils.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class nombreprincipal {
    private SessionManager sessionManager;
    public nombreprincipal(){}
    public String obtenerNombreUsuario(Context context) {
        sessionManager = new SessionManager(context);
        String idUsuario = sessionManager.obtenerId();
        String nombre = null;

        try {
            // Establecer la conexión a la base de datos SQL Server
            Connection connection = ConexionDB.obtenerConexion();

            // Ejecutar la consulta SQL para obtener el nombre del usuario según su ID
            String sql = "SELECT personal.nombre FROM usuario INNER JOIN personal ON usuario.id_personal = personal.id_personal WHERE usuario.id_usuario = "+idUsuario;

            PreparedStatement statement = connection.prepareStatement(sql);
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
