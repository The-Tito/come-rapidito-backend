package org.alilopez.model;

public class DetailCart {
    private int id_producto;
    private int id_carrito;
    private int cantidad;
    private float precio_unitario;

    public DetailCart() {
    }

    public DetailCart(int id_producto, int id_carrito, int cantidad, float precio_unitario) {
        this.id_producto = id_producto;
        this.id_carrito = id_carrito;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(int id_carrito) {
        this.id_carrito = id_carrito;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(float precio_unitario) {
        this.precio_unitario = precio_unitario;
    }
}
