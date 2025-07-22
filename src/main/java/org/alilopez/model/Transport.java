package org.alilopez.model;

public class Transport {
    private int id_trasport;
    private String url_foto_vehicle;
    private String placas;
    private int id_usuario;
    private int id_vehiculo;

    public Transport() {
    }

    public Transport(int id_trasport, String url_foto_vehicle, String placas, int id_usuario, int id_vehiculo) {
        this.id_trasport = id_trasport;
        this.url_foto_vehicle = url_foto_vehicle;
        this.placas = placas;
        this.id_usuario = id_usuario;
        this.id_vehiculo = id_vehiculo;
    }

    public int getId_trasport() {
        return id_trasport;
    }

    public void setId_trasport(int id_trasport) {
        this.id_trasport = id_trasport;
    }

    public String getUrl_foto_vehicle() {
        return url_foto_vehicle;
    }

    public void setUrl_foto_vehicle(String url_foto_vehicle) {
        this.url_foto_vehicle = url_foto_vehicle;
    }

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }
}
