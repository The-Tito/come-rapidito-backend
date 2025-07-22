package org.alilopez.model;

public class AuthResponse {
    private String nombre;
    private String token;
    private int idRol;
    private int id_usuario;

    public AuthResponse(){};

    public AuthResponse(String nombre, String token, int idRol, int id_usuario) {
        this.nombre = nombre;
        this.token = token;
        this.idRol = idRol;
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
}
