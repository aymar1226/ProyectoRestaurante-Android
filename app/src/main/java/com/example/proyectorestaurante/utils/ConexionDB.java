package com.example.proyectorestaurante.utils;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String DB_URL = "jdbc:jtds:sqlserver://192.168.18.115:1433/segurity_app";
    private static final String DB_USER = "user1";
    private static final String DB_PASSWORD = "1234";

    public static Connection obtenerConexion() {
        Connection cnn = null;

        try {
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            cnn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnn;
    }
}
