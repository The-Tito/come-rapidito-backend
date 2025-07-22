package org.alilopez.service;

import org.alilopez.DTO.Order.*;
import org.alilopez.DTO.OrderResponse.Delivery.OrderDeliveryResponse;
import org.alilopez.DTO.OrderResponse.Restaurant.OrderRestaurantResponse;
import org.alilopez.DTO.OrderResponse.User.OrderUserResponse;
import org.alilopez.model.Cart;
import org.alilopez.model.DetailCart;
import org.alilopez.model.Order;
import org.alilopez.repository.CartRepository;
import org.alilopez.repository.DetailCartRepository;
import org.alilopez.repository.OrderRepository;

import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private OrderRepository orderRepository;
    private CartRepository cartRepository;
    private DetailCartRepository detailCartRepository;
    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, DetailCartRepository detailCartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.detailCartRepository = detailCartRepository;
    }

    public OrderResponseDTO create(OrderRequestDTO orderRequestDTO) throws SQLException {

        Cart cart = new Cart(orderRequestDTO.id_usuario, Float.valueOf(orderRequestDTO.carrito.total));
        int id_carrito = cartRepository.createCarrito(cart);

        for (DetailCartDTO detailCartDTO : orderRequestDTO.carrito.detalleCarrito){
            DetailCart detailCart = new DetailCart();
            detailCart.setId_carrito(id_carrito);
            detailCart.setId_producto(detailCartDTO.id_producto);
            detailCart.setCantidad(detailCartDTO.cantidad);
            detailCart.setPrecio_unitario(detailCartDTO.precio_unitario);
            detailCartRepository.save(detailCart);
        }
        Order saveOrder = new Order();
        saveOrder.setId_usuario(orderRequestDTO.id_usuario);
        saveOrder.setId_direccion(orderRequestDTO.id_direccion);
        saveOrder.setId_carrito(id_carrito);
        saveOrder.setTotal(orderRequestDTO.carrito.total);
        saveOrder.setId_status(orderRequestDTO.id_status);
        saveOrder.setTarifa(orderRequestDTO.tarifa);
        saveOrder.setId_restaurante(orderRequestDTO.id_restaurante);
        OrderResponseDTO responseDTO = orderRepository.save(saveOrder);
        return responseDTO;
    }

    public void updateStatus(int id_pedido, UpdateStatusRequest updateStatusRequest) throws SQLException {
        orderRepository.updateStatus(id_pedido, updateStatusRequest);
    }

    public void updateFee(int id_pedido, UpdateFeeRequest updateFeeRequest) throws SQLException {
        orderRepository.updateFee(id_pedido, updateFeeRequest);
    }

    public List<OrderRestaurantResponse> getOrderByRestaurantId(int id_restaurante) throws SQLException {
        List<OrderRestaurantResponse> response = orderRepository.getOrderByRestaurantId(id_restaurante);
        return response;
    }
    public List<OrderRestaurantResponse> getOrdersRestaurantHistory(int id_restaurante) throws SQLException {
        return orderRepository.getOrdersRestaurantHistory(id_restaurante);
    }

    public List<OrderDeliveryResponse> findActiveOrders() throws SQLException {
        return orderRepository.findActiveOrders();
    }

    public List<OrderDeliveryResponse> getHistory() throws SQLException {
        return orderRepository.getHistory();
    }

    public List<OrderUserResponse> historyOrdersByIdUser(int id_usuario) throws SQLException {
        return orderRepository.historyOrdersByIdUser(id_usuario);
    }
    public List<OrderUserResponse> ActiveOrdersByIdUser(int id_usuario) throws SQLException {
        return orderRepository.ActiveOrdersByIdUser(id_usuario);
    }
    public OrderUserResponse actualOrder(int id_pedido) throws SQLException {
        return orderRepository.actualOrder(id_pedido);
    }
}
