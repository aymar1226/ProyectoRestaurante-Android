package com.example.proyectorestaurante.auth;

import android.content.Context;

import com.example.proyectorestaurante.utils.ConexionDB;
import com.example.proyectorestaurante.utils.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class classlogin {
    private SessionManager sessionManager;
    Context mContext;
    private String usuario;
    private String password;

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
    public classlogin(Context context){
        mContext=context;
    }

    public boolean validarCredenciales(String usuario, String password){
        sessionManager = new SessionManager(mContext);
        try(Connection connection = ConexionDB.obtenerConexion()){
            String query = "SELECT COUNT(*), r.nombre_rol FROM usuario u JOIN rol r ON u.id_rol = r.id_rol WHERE u.correo = ? AND CAST(DECRYPTBYPASSPHRASE('L4f4ry3t3nCrypt4d0', u.contraseña) as VARCHAR(MAX)) = ? GROUP BY r.nombre_rol";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, usuario);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                String rol = resultSet.getString("nombre_rol");
                sessionManager.guardarRol(rol);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
