package org.alilopez.DTO.OrderResponse.User;

import org.alilopez.DTO.Order.CartDTO;
import org.alilopez.DTO.OrderResponse.Delivery.RestaurantResponse;
import org.alilopez.DTO.OrderResponse.Restaurant.CartRestaurantResponse;
import org.alilopez.model.Address;

public class OrderUserResponse {
    private int id_pedido;
    private int id_status;
    private String fecha_pedido;
    private int tarifa;
    private float totalFinal;
    private String nombre;
    private Address direccion;
    private RestaurantResponse restaurante;
    private CartRestaurantResponse carrito;
    private int puntuacion_pedido;


    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_status() {
        return id_status;
    }

    public void setId_status(int id_status) {
        this.id_status = id_status;
    }

    public String getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(String fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public float getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(float totalFinal) {
        this.totalFinal = totalFinal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Address getDireccion() {
        return direccion;
    }

    public void setDireccion(Address direccion) {
        this.direccion = direccion;
    }

    public RestaurantResponse getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(RestaurantResponse restaurante) {
        this.restaurante = restaurante;
    }

    public CartRestaurantResponse getCarrito() {
        return carrito;
    }

    public void setCarrito(CartRestaurantResponse carrito) {
        this.carrito = carrito;
    }

    public int getPuntuacion_pedido() {
        return puntuacion_pedido;
    }

    public void setPuntuacion_pedido(int puntuacion_pedido) {
        this.puntuacion_pedido = puntuacion_pedido;
    }

    public int getTarifa() {
        return tarifa;
    }

    public void setTarifa(int tarifa) {
        this.tarifa = tarifa;
    }
}
