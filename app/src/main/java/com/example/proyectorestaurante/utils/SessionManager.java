package com.example.proyectorestaurante.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MySession";
    private static final String KEY_ROL = "rol";
    private static final String KEY_ID = "idSession";

    private static final String KEY_LOGIN = "logueado";


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void guardarRol(String rol) {
        editor.putString(KEY_ROL, rol);
        editor.apply();
    }

    public String obtenerRol() {
        return preferences.getString(KEY_ROL, null);
    }

    public void guardarId(String id) {
        editor.putString(KEY_ID, id);
        editor.apply();
    }

    public String obtenerId() {
        return preferences.getString(KEY_ID, null);
    }



    public void guardarSession(boolean isLoggedIn) {
        editor.putBoolean(KEY_LOGIN, isLoggedIn);
        editor.apply();
    }
    public boolean obtenerSession() {
        return preferences.getBoolean(KEY_LOGIN, false);
    }

    public void cerrarSesion() {
        editor.remove(KEY_ROL);
        editor.remove(KEY_ID);
        editor.remove(KEY_LOGIN);
        editor.apply();
    }
}
