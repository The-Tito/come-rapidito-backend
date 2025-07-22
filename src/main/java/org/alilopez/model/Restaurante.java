package org.alilopez.model;

import java.sql.Time;

public class Restaurante {
    private int id_restaurante;
    private String logo_url;
    private String banner_url;
    private String nombre_restaurante;
    private String telefono;
    private String direccion;
    private Time horario_apertura;
    private Time horario_cierre;
    private int id_usuario;
    private String nombre_usuario;
    private String telefono_usuario;


    public int getId_restaurante() {
        return id_restaurante;
    }

    public void setId_restaurante(int id_restaurante) {
        this.id_restaurante = id_restaurante;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getNombre_restaurante() {
        return nombre_restaurante;
    }

    public void setNombre_restaurante(String nombre_restaurante) {
        this.nombre_restaurante = nombre_restaurante;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Time getHorario_apertura() {
        return horario_apertura;
    }

    public void setHorario_apertura(Time horario_apertura) {
        this.horario_apertura = horario_apertura;
    }

    public Time getHorario_cierre() {
        return horario_cierre;
    }

    public void setHorario_cierre(Time horario_cierre) {
        this.horario_cierre = horario_cierre;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getTelefono_usuario() {
        return telefono_usuario;
    }

    public void setTelefono_usuario(String telefono_usuario) {
        this.telefono_usuario = telefono_usuario;
    }
}
