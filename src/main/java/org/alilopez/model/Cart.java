package org.alilopez.model;

import java.sql.Date;
import java.sql.Time;

public class Cart {
   private int id_usuario;
   private float total;

    public Cart() {
    }

    public Cart(int id_usuario, float total) {
        this.id_usuario = id_usuario;
        this.total = total;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
