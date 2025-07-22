package org.alilopez.DTO.OrderResponse.Restaurant;

import java.util.List;

public class CartRestaurantResponse {
    public float total;
    public List<DetailCartResponse> detalleCarrito;

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<DetailCartResponse> getDetalleCarrito() {
        return detalleCarrito;
    }

    public void setDetalleCarrito(List<DetailCartResponse> detalleCarrito) {
        this.detalleCarrito = detalleCarrito;
    }
}
