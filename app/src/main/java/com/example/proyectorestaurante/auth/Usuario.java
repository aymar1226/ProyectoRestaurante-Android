package com.example.proyectorestaurante.auth;

import com.example.proyectorestaurante.utils.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {
    private String nombre;
    private String apellido;
    private String correo;
    private String dni;

    public Usuario() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public static Usuario obtenerUsuario(int id) {
        Usuario usuario = null;

        try {
            // Establecer la conexión a la base de datos SQL Server
            Connection connection = ConexionDB.obtenerConexion();

            // Ejecutar la consulta SQL para obtener los datos del usuario según su ID
            String sql = "SELECT nombre, apellido, nombre_usuario, dni FROM usuarios WHERE id_usuario = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Obtener los datos del usuario si se encuentra
            if (resultSet.next()) {
                usuario = new Usuario();
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setApellido(resultSet.getString("apellido"));
                usuario.setCorreo(resultSet.getString("nombre_usuario"));
                usuario.setDni(resultSet.getString("dni"));
            }

            // Cerrar los recursos
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }
}
