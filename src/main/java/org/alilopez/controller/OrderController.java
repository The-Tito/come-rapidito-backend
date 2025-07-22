package org.alilopez.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.alilopez.DTO.Order.OrderRequestDTO;
import org.alilopez.DTO.Order.OrderResponseDTO;
import org.alilopez.DTO.Order.UpdateFeeRequest;
import org.alilopez.DTO.Order.UpdateStatusRequest;
import org.alilopez.DTO.OrderResponse.Delivery.OrderDeliveryResponse;
import org.alilopez.DTO.OrderResponse.Restaurant.OrderRestaurantResponse;
import org.alilopez.DTO.OrderResponse.Restaurant.StatusRequest;
import org.alilopez.DTO.OrderResponse.User.OrderUserResponse;
import org.alilopez.service.OrderService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderController {
    private OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrder(Context ctx ) {
        try {
            OrderRequestDTO orderRequestDTO = ctx.bodyAsClass(OrderRequestDTO.class);
            OrderResponseDTO responseDTO = orderService.create(orderRequestDTO);
            ctx.status(HttpStatus.CREATED).json(responseDTO);
        }catch (Exception e){
            ctx.status(HttpStatus.BAD_REQUEST).json("Error: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateStatus(Context ctx ) {
        try {
            int id_pedido = ctx.pathParamAsClass("id", Integer.class).get();
            UpdateStatusRequest updateStatusRequest = ctx.bodyAsClass(UpdateStatusRequest.class);
            orderService.updateStatus(id_pedido, updateStatusRequest);
            ctx.status(HttpStatus.OK).json(Map.of("message", "Status actualizado exitosamente"));
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json("Error: "+ e.getMessage());

        }
    }

    public void updateFee(Context ctx ) {
        try {
            int id_pedido = ctx.pathParamAsClass("id", Integer.class).get();
            UpdateFeeRequest updateFeeRequest = ctx.bodyAsClass(UpdateFeeRequest.class);
            orderService.updateFee(id_pedido, updateFeeRequest);
            ctx.status(HttpStatus.OK).json("Tarifa actualizado exitosamente");
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json("Error: "+ e.getMessage());
        }
    }

    public void ativeOrderByRestaurantId(Context ctx ) {
        try {
            int id_restaurante = Integer.parseInt(ctx.pathParam("id"));

            List<OrderRestaurantResponse> response = orderService.getOrderByRestaurantId(id_restaurante);
            ctx.status(HttpStatus.OK).json(response);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json("Error: "+ e.getMessage());
        }
    }

    public void historyOrdersRestaurant(Context ctx ) throws SQLException {
        try {
            int id_restaurante = ctx.pathParamAsClass("id", Integer.class).get();
            List<OrderRestaurantResponse> response = orderService.getOrdersRestaurantHistory(id_restaurante);
            ctx.status(HttpStatus.OK).json(response);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json("Error: "+ e.getMessage());
        }
    }

    public void getActiveOrders(Context ctx ) {
        try {

            List<OrderDeliveryResponse> response = orderService.findActiveOrders();
            ctx.status(HttpStatus.OK).json(response);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json("Error: "+ e.getMessage());
        }
    }

    public void getHistory(Context ctx ) {
        try {
            List<OrderDeliveryResponse> response = orderService.getHistory();
            ctx.status(HttpStatus.OK).json(response);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json("Error: "+ e.getMessage());
        }
    }

    public void historyOrdersByIdUser(Context ctx ) {
        try {
            int id_usuario = ctx.pathParamAsClass("id", Integer.class).get();

            List<OrderUserResponse> response = orderService.historyOrdersByIdUser(id_usuario);
            ctx.status(HttpStatus.OK).json(response);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json("Error: "+ e.getMessage());
        }
    }

    public void activeOrdersByIdUser(Context ctx ) {
        try {
            int id_usuario = ctx.pathParamAsClass("id", Integer.class).get();
            List<OrderUserResponse> response = orderService.ActiveOrdersByIdUser(id_usuario);
            ctx.status(HttpStatus.OK).json(response);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json("Error: "+ e.getMessage());
        }
    }
    public void actualOrder(Context ctx ) {
        try {
            int id_pedido = ctx.pathParamAsClass("id", Integer.class).get();
            OrderUserResponse orderUserResponse = orderService.actualOrder(id_pedido);
            ctx.status(HttpStatus.OK).json(orderUserResponse);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json("Error: "+ e.getMessage());
        }
    }
}
