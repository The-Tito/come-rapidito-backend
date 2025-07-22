package org.alilopez.DTO.OrderResponse.Restaurant;

public class OrderRestaurantResponse {
    private int id_pedido;
    private int id_status;
    private String fecha_pedido;
    private CartRestaurantResponse carrito;



    public OrderRestaurantResponse(int id_pedido, int id_status, String fecha_pedido, CartRestaurantResponse carrito) {
        this.id_pedido = id_pedido;
        this.id_status = id_status;
        this.fecha_pedido = fecha_pedido;
        this.carrito = carrito;
    }

    public OrderRestaurantResponse() {
    }

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

    public CartRestaurantResponse getCarrito() {
        return carrito;
    }

    public void setCarrito(CartRestaurantResponse carrito) {
        this.carrito = carrito;
    }
}
