package com.example.proyectorestaurante.Clases;

import com.example.proyectorestaurante.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class classlogin {
    private String usuario, password;

    private static String rol;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public classlogin(){}

    public static boolean validarCredenciales(String usuario, String password){
        try(Connection connection = ConexionDB.obtenerConexion()){
            String query = "SELECT COUNT(*), r.nombre_rol FROM usuario u JOIN rol r ON u.id_rol = r.id_rol WHERE u.correo = ? AND CAST(DECRYPTBYPASSPHRASE('L4f4ry3t3nCrypt4d0', u.contraseña) as VARCHAR(MAX)) = ? GROUP BY r.nombre_rol";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, usuario);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                rol = resultSet.getString("nombre_rol");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getRol() {
        return rol;
    }
}
