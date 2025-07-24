package org.alilopez.repository;

import org.alilopez.DTO.Order.OrderResponseDTO;
import org.alilopez.DTO.Order.UpdateFeeRequest;
import org.alilopez.DTO.Order.UpdateStatusRequest;
import org.alilopez.DTO.OrderResponse.Delivery.OrderDeliveryResponse;
import org.alilopez.DTO.OrderResponse.Delivery.RestaurantResponse;
import org.alilopez.DTO.OrderResponse.Restaurant.CartRestaurantResponse;
import org.alilopez.DTO.OrderResponse.Restaurant.DetailCartResponse;
import org.alilopez.DTO.OrderResponse.Restaurant.OrderRestaurantResponse;
import org.alilopez.DTO.OrderResponse.User.OrderUserResponse;
import org.alilopez.config.DatabaseConfig;
import org.alilopez.model.Address;
import org.alilopez.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    public OrderResponseDTO save(Order saveOrder) throws SQLException{
        OrderResponseDTO responseDTO;
        int id_pedido = 0;
        String query = "INSERT INTO pedidos (id_usuario, id_direccion, id_carrito, total, tarifa, id_status, id_restaurante) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            stmt.setInt(1, saveOrder.getId_usuario());
            stmt.setInt(2, saveOrder.getId_direccion());
            stmt.setInt(3, saveOrder.getId_carrito());
            stmt.setDouble(4, saveOrder.getTotal());
            stmt.setDouble(5, saveOrder.getTarifa());
            stmt.setInt(6, saveOrder.getId_status());
            stmt.setInt(7, saveOrder.getId_restaurante());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                id_pedido = rs.getInt(1);
            }
        }
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId_pedido(id_pedido);
        response.setId_usuario(saveOrder.getId_usuario());
        response.setId_direccion(saveOrder.getId_direccion());
        response.setId_carrito(saveOrder.getId_carrito());
        response.setId_status(saveOrder.getId_status());
        response.setTotal(saveOrder.getTotal());
        response.setId_status(saveOrder.getId_status());
        response.setId_restaurante(saveOrder.getId_restaurante());
        return response;
    }

    public void updateStatus(int id_pedido, UpdateStatusRequest updateStatusRequest) throws SQLException {
        String query = "UPDATE pedidos SET id_status = ? WHERE id_pedido = ? ";
        try(Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, updateStatusRequest.getId_status());
            stmt.setInt(2, id_pedido);
            stmt.executeUpdate();
        }
    }

    public void updateFee(int id_pedido, UpdateFeeRequest updateFeeRequest) throws SQLException {
        String query = "UPDATE pedidos SET tarifa = ? WHERE id_pedido = ? ";
        try(Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setDouble(1, updateFeeRequest.getTarifa());
            stmt.setInt(2, id_pedido);
            stmt.executeUpdate();
        }
    }

    public List<OrderRestaurantResponse> getOrdersRestaurantHistory(int id_restaurante) throws SQLException {
        List<OrderRestaurantResponse> response = new ArrayList<>();
        int id_carrito = 0;
        String query = "SELECT pedidos.id_pedido, pedidos.id_status, pedidos.fecha_pedido, carritos.total, pedidos.id_carrito FROM pedidos  INNER JOIN carritos ON pedidos.id_carrito = carritos.id_carrito WHERE id_restaurante = ? AND pedidos.id_status = 4 OR pedidos.id_status = 5;";
        String queryCart = "SELECT  detalles_carrito.cantidad, productos.nombre FROM detalles_carrito INNER JOIN productos ON detalles_carrito.id_producto = productos.id_producto WHERE id_carrito = ? ";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id_restaurante);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                OrderRestaurantResponse orderRestaurantResponse = new OrderRestaurantResponse();;
                CartRestaurantResponse cartRestaurantResponse = new CartRestaurantResponse();
                List<DetailCartResponse> detailCartResponseList = new ArrayList<>();
                DetailCartResponse detailCartResponse;
                orderRestaurantResponse.setId_pedido(rs.getInt("id_pedido"));
                orderRestaurantResponse.setId_status(rs.getInt("id_status"));
                orderRestaurantResponse.setFecha_pedido(rs.getString("fecha_pedido"));
                cartRestaurantResponse.setTotal(rs.getFloat("total"));
                id_carrito = rs.getInt("id_carrito");
                try(Connection conn2 = DatabaseConfig.getDataSource().getConnection();
                    PreparedStatement stmt2 = conn2.prepareStatement(queryCart)){
                    stmt2.setInt(1, id_carrito);
                    ResultSet rs2 = stmt2.executeQuery();
                    while(rs2.next()){
                        detailCartResponse = new DetailCartResponse();
                        detailCartResponse.setNombre(rs2.getString("nombre"));
                        detailCartResponse.setCantidad(rs2.getInt("cantidad"));
                        detailCartResponseList.add(detailCartResponse);
                    }
                }
                cartRestaurantResponse.setDetalleCarrito(detailCartResponseList);
                orderRestaurantResponse.setCarrito(cartRestaurantResponse);
                response.add(orderRestaurantResponse);
            }
        }
        return response;
    }

    public List<OrderRestaurantResponse> getOrderByRestaurantId(int id_restaurante) throws SQLException {
        List<OrderRestaurantResponse> response = new ArrayList<>();
        int id_carrito = 0;
        String query = "SELECT pedidos.id_pedido, pedidos.id_status, pedidos.fecha_pedido, carritos.total, pedidos.id_carrito FROM pedidos  INNER JOIN carritos ON pedidos.id_carrito = carritos.id_carrito WHERE id_restaurante = ? AND pedidos.id_status != 4 AND pedidos.id_status != 5;";
        String queryCart = "SELECT  detalles_carrito.cantidad, productos.nombre FROM detalles_carrito INNER JOIN productos ON detalles_carrito.id_producto = productos.id_producto WHERE id_carrito = ? ";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id_restaurante);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                OrderRestaurantResponse orderRestaurantResponse = new OrderRestaurantResponse();;
                CartRestaurantResponse cartRestaurantResponse = new CartRestaurantResponse();
                List<DetailCartResponse> detailCartResponseList = new ArrayList<>();
                DetailCartResponse detailCartResponse;
                orderRestaurantResponse.setId_pedido(rs.getInt("id_pedido"));
                orderRestaurantResponse.setId_status(rs.getInt("id_status"));
                orderRestaurantResponse.setFecha_pedido(rs.getString("fecha_pedido"));
                cartRestaurantResponse.setTotal(rs.getFloat("total"));
                id_carrito = rs.getInt("id_carrito");
                try(Connection conn2 = DatabaseConfig.getDataSource().getConnection();
                    PreparedStatement stmt2 = conn2.prepareStatement(queryCart)){
                    stmt2.setInt(1, id_carrito);
                    ResultSet rs2 = stmt2.executeQuery();
                    while(rs2.next()){
                        detailCartResponse = new DetailCartResponse();
                        detailCartResponse.setNombre(rs2.getString("nombre"));
                        detailCartResponse.setCantidad(rs2.getInt("cantidad"));
                        detailCartResponseList.add(detailCartResponse);
                    }
                }
                cartRestaurantResponse.setDetalleCarrito(detailCartResponseList);
                orderRestaurantResponse.setCarrito(cartRestaurantResponse);
                response.add(orderRestaurantResponse);
            }
        }
        return response;
    }

    public List<OrderDeliveryResponse> findActiveOrders() throws SQLException {
        List<OrderDeliveryResponse> orderDeliveryResponseList = new ArrayList<>();
        String query =  "SELECT pedidos.id_pedido, pedidos.id_status, pedidos.fecha_pedido, pedidos.total, usuarios.nombre, usuarios.numero_telefono, " +
                "direcciones.calle, direcciones.colonia, direcciones.numero_casa, direcciones.codigo_postal, direcciones.referencia, " +
                "restaurantes.nombre_restaurante, restaurantes.direccion, restaurantes.logo_url " +
                "FROM pedidos " +
                "INNER JOIN usuarios ON pedidos.id_usuario = usuarios.id_usuario " +
                "INNER JOIN direcciones ON pedidos.id_direccion = direcciones.id_direccion " +
                "INNER JOIN restaurantes ON pedidos.id_restaurante = restaurantes.id_restaurante " +
                "WHERE pedidos.id_status != 4 AND pedidos.id_status != 5;";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                OrderDeliveryResponse orderDeliveryResponse = new OrderDeliveryResponse();
                Address address = new Address();
                RestaurantResponse restaurantResponse = new RestaurantResponse();
                address.setCalle(rs.getString("calle"));
                address.setColonia(rs.getString("colonia"));
                address.setNumero_casa(rs.getString("numero_casa"));
                address.setCodigo_postal(rs.getString("codigo_postal"));
                address.setReferencia(rs.getString("referencia"));
                restaurantResponse.setNombre_restaurante(rs.getString("nombre_restaurante"));
                restaurantResponse.setDireccion(rs.getString("direccion"));
                restaurantResponse.setLogo_url(rs.getString("logo_url"));
                orderDeliveryResponse.setId_pedido(rs.getInt("id_pedido"));
                orderDeliveryResponse.setId_status(rs.getInt("id_status"));
                orderDeliveryResponse.setFecha_pedido(rs.getString("fecha_pedido"));
                orderDeliveryResponse.setTotal(rs.getFloat("total"));
                orderDeliveryResponse.setNombre(rs.getString("nombre"));
                orderDeliveryResponse.setNumero_telefono(rs.getString("numero_telefono"));
                orderDeliveryResponse.setDireccion(address);
                orderDeliveryResponse.setRestaurante(restaurantResponse);
                orderDeliveryResponseList.add(orderDeliveryResponse);

            }
        }
        return orderDeliveryResponseList;
    }

    public List<OrderDeliveryResponse> getHistory() throws SQLException {
        List<OrderDeliveryResponse> orderDeliveryResponseList = new ArrayList<>();
        String query =  "SELECT pedidos.id_pedido, pedidos.id_status, pedidos.fecha_pedido, pedidos.total, usuarios.nombre, usuarios.numero_telefono, " +
                "direcciones.calle, direcciones.colonia, direcciones.numero_casa, direcciones.codigo_postal, direcciones.referencia, " +
                "restaurantes.nombre_restaurante, restaurantes.direccion, restaurantes.logo_url " +
                "FROM pedidos " +
                "INNER JOIN usuarios ON pedidos.id_usuario = usuarios.id_usuario " +
                "INNER JOIN direcciones ON pedidos.id_direccion = direcciones.id_direccion " +
                "INNER JOIN restaurantes ON pedidos.id_restaurante = restaurantes.id_restaurante " +
                "WHERE pedidos.id_status =4 AND pedidos.id_status = 5 ;";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                OrderDeliveryResponse orderDeliveryResponse = new OrderDeliveryResponse();
                Address address = new Address();
                RestaurantResponse restaurantResponse = new RestaurantResponse();
                address.setCalle(rs.getString("calle"));
                address.setColonia(rs.getString("colonia"));
                address.setNumero_casa(rs.getString("numero_casa"));
                address.setCodigo_postal(rs.getString("codigo_postal"));
                address.setReferencia(rs.getString("referencia"));
                restaurantResponse.setNombre_restaurante(rs.getString("nombre_restaurante"));
                restaurantResponse.setDireccion(rs.getString("direccion"));
                restaurantResponse.setLogo_url(rs.getString("logo_url"));
                orderDeliveryResponse.setId_pedido(rs.getInt("id_pedido"));
                orderDeliveryResponse.setId_status(rs.getInt("id_status"));
                orderDeliveryResponse.setFecha_pedido(rs.getString("fecha_pedido"));
                orderDeliveryResponse.setTotal(rs.getFloat("total"));
                orderDeliveryResponse.setNombre(rs.getString("nombre"));
                orderDeliveryResponse.setNumero_telefono(rs.getString("numero_telefono"));
                orderDeliveryResponse.setDireccion(address);
                orderDeliveryResponse.setRestaurante(restaurantResponse);
                orderDeliveryResponseList.add(orderDeliveryResponse);

            }
        }
        return orderDeliveryResponseList;
    }
    public List<OrderUserResponse> historyOrdersByIdUser(int id_usuario) throws SQLException {
        List<OrderUserResponse> orderUserResponseList = new ArrayList<>();

        String query = "SELECT pedidos.tarifa, pedidos.id_pedido, pedidos.id_status, pedidos.fecha_pedido, pedidos.total, pedidos.id_carrito, usuarios.nombre, " +
                "direcciones.calle, direcciones.colonia, direcciones.numero_casa, direcciones.codigo_postal, direcciones.referencia, " +
                "restaurantes.nombre_restaurante, restaurantes.direccion " +
                "FROM pedidos " +
                "INNER JOIN usuarios ON pedidos.id_usuario = usuarios.id_usuario " +
                "INNER JOIN direcciones ON pedidos.id_direccion = direcciones.id_direccion " +
                "INNER JOIN restaurantes ON pedidos.id_restaurante = restaurantes.id_restaurante " +
                "WHERE pedidos.id_usuario = ? AND pedidos.id_status = 4 OR pedidos.id_status = 5;";

        String queryCart = "SELECT detalles_carrito.cantidad, productos.nombre " +
                "FROM detalles_carrito " +
                "INNER JOIN productos ON detalles_carrito.id_producto = productos.id_producto " +
                "WHERE id_carrito = ?";
        String queryReview = "SELECT * FROM valoraciones WHERE id_pedido = ?;";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id_usuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderUserResponse orderUserResponse = new OrderUserResponse();
                Address address = new Address();
                RestaurantResponse restaurantResponse = new RestaurantResponse();
                CartRestaurantResponse cartRestaurantResponse = new CartRestaurantResponse();

                //Crear nueva lista para cada pedido
                List<DetailCartResponse> detailCartResponseList = new ArrayList<>();

                // Obtener el id_carrito y id_pedido del resultado actual
                int id_carrito = rs.getInt("id_carrito");
                int id_pedido = rs.getInt("id_pedido");
                // Configurar dirección
                address.setCalle(rs.getString("calle"));
                address.setColonia(rs.getString("colonia"));
                address.setNumero_casa(rs.getString("numero_casa"));
                address.setCodigo_postal(rs.getString("codigo_postal"));
                address.setReferencia(rs.getString("referencia"));

                // Configurar restaurante
                restaurantResponse.setNombre_restaurante(rs.getString("nombre_restaurante"));
                restaurantResponse.setDireccion(rs.getString("direccion"));

                // Configurar orden principal
                orderUserResponse.setId_pedido(rs.getInt("id_pedido"));
                orderUserResponse.setId_status(rs.getInt("id_status"));
                orderUserResponse.setFecha_pedido(rs.getString("fecha_pedido"));
                orderUserResponse.setTotalFinal(rs.getFloat("total"));
                orderUserResponse.setNombre(rs.getString("nombre"));
                orderUserResponse.setTarifa(rs.getInt("tarifa"));
                cartRestaurantResponse.setTotal(rs.getFloat("total"));
                orderUserResponse.setDireccion(address);
                orderUserResponse.setRestaurante(restaurantResponse);

                // Obtener detalles del carrito
                try (Connection conn2 = DatabaseConfig.getDataSource().getConnection();
                     PreparedStatement stmt2 = conn2.prepareStatement(queryCart)) {

                    stmt2.setInt(1, id_carrito);
                    ResultSet rs2 = stmt2.executeQuery();

                    while (rs2.next()) {
                        DetailCartResponse detailCartResponse = new DetailCartResponse();
                        detailCartResponse.setNombre(rs2.getString("nombre"));
                        detailCartResponse.setCantidad(rs2.getInt("cantidad"));
                        detailCartResponseList.add(detailCartResponse);
                    }

                    cartRestaurantResponse.setDetalleCarrito(detailCartResponseList);
                }
                try (Connection conn3 = DatabaseConfig.getDataSource().getConnection();
                     PreparedStatement stmt3 = conn3.prepareStatement(queryReview)) {
                    stmt3.setInt(1, id_pedido);
                    ResultSet rs3 = stmt3.executeQuery();
                    if (rs3.next()) {
                        orderUserResponse.setPuntuacion_pedido(rs3.getInt("puntuacion_pedido"));
                    }

                }
                orderUserResponse.setCarrito(cartRestaurantResponse);
                orderUserResponseList.add(orderUserResponse);
            }
        }

        return orderUserResponseList;
    }
    public List<OrderUserResponse> ActiveOrdersByIdUser(int id_usuario) throws SQLException {
        List<OrderUserResponse> orderUserResponseList = new ArrayList<>();

        String query = "SELECT pedidos.id_pedido, pedidos.id_status, pedidos.fecha_pedido, pedidos.total, pedidos.id_carrito, usuarios.nombre, " +
                "direcciones.calle, direcciones.colonia, direcciones.numero_casa, direcciones.codigo_postal, direcciones.referencia, " +
                "restaurantes.nombre_restaurante, restaurantes.direccion " +
                "FROM pedidos " +
                "INNER JOIN usuarios ON pedidos.id_usuario = usuarios.id_usuario " +
                "INNER JOIN direcciones ON pedidos.id_direccion = direcciones.id_direccion " +
                "INNER JOIN restaurantes ON pedidos.id_restaurante = restaurantes.id_restaurante " +
                "WHERE pedidos.id_usuario = ? AND pedidos.id_status != 4 AND pedidos.id_status != 5;";

        String queryCart = "SELECT detalles_carrito.cantidad, productos.nombre " +
                "FROM detalles_carrito " +
                "INNER JOIN productos ON detalles_carrito.id_producto = productos.id_producto " +
                "WHERE id_carrito = ?";
        String queryReview = "SELECT * FROM valoraciones WHERE id_pedido = ?;";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id_usuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderUserResponse orderUserResponse = new OrderUserResponse();
                Address address = new Address();
                RestaurantResponse restaurantResponse = new RestaurantResponse();
                CartRestaurantResponse cartRestaurantResponse = new CartRestaurantResponse();

                //Crear nueva lista para cada pedido
                List<DetailCartResponse> detailCartResponseList = new ArrayList<>();

                // Obtener el id_carrito y id_pedido del resultado actual
                int id_carrito = rs.getInt("id_carrito");
                int id_pedido = rs.getInt("id_pedido");
                // Configurar dirección
                address.setCalle(rs.getString("calle"));
                address.setColonia(rs.getString("colonia"));
                address.setNumero_casa(rs.getString("numero_casa"));
                address.setCodigo_postal(rs.getString("codigo_postal"));
                address.setReferencia(rs.getString("referencia"));

                // Configurar restaurante
                restaurantResponse.setNombre_restaurante(rs.getString("nombre_restaurante"));
                restaurantResponse.setDireccion(rs.getString("direccion"));

                // Configurar orden principal
                orderUserResponse.setId_pedido(rs.getInt("id_pedido"));
                orderUserResponse.setId_status(rs.getInt("id_status"));
                orderUserResponse.setFecha_pedido(rs.getString("fecha_pedido"));
                orderUserResponse.setTotalFinal(rs.getFloat("total"));
                orderUserResponse.setNombre(rs.getString("nombre"));
                cartRestaurantResponse.setTotal(rs.getFloat("total"));
                orderUserResponse.setDireccion(address);
                orderUserResponse.setRestaurante(restaurantResponse);

                // Obtener detalles del carrito
                try (Connection conn2 = DatabaseConfig.getDataSource().getConnection();
                     PreparedStatement stmt2 = conn2.prepareStatement(queryCart)) {

                    stmt2.setInt(1, id_carrito);
                    ResultSet rs2 = stmt2.executeQuery();

                    while (rs2.next()) {
                        DetailCartResponse detailCartResponse = new DetailCartResponse();
                        detailCartResponse.setNombre(rs2.getString("nombre"));
                        detailCartResponse.setCantidad(rs2.getInt("cantidad"));
                        detailCartResponseList.add(detailCartResponse);
                    }

                    cartRestaurantResponse.setDetalleCarrito(detailCartResponseList);
                }
                try (Connection conn3 = DatabaseConfig.getDataSource().getConnection();
                     PreparedStatement stmt3 = conn3.prepareStatement(queryReview)) {
                    stmt3.setInt(1, id_pedido);
                    ResultSet rs3 = stmt3.executeQuery();
                    if (rs3.next()) {
                        orderUserResponse.setPuntuacion_pedido(rs3.getInt("puntuacion_pedido"));
                    }

                }
                orderUserResponse.setCarrito(cartRestaurantResponse);
                orderUserResponseList.add(orderUserResponse);
            }
        }

        return orderUserResponseList;
    }

    public OrderUserResponse actualOrder(int id_pedido) throws SQLException {
        OrderUserResponse orderUserResponse = new OrderUserResponse();

        String query = "SELECT pedidos.tarifa, pedidos.id_pedido, pedidos.id_status, pedidos.fecha_pedido, pedidos.total, pedidos.id_carrito, usuarios.nombre, " +
                "direcciones.calle, direcciones.colonia, direcciones.numero_casa, direcciones.codigo_postal, direcciones.referencia, " +
                "restaurantes.nombre_restaurante, restaurantes.direccion " +
                "FROM pedidos " +
                "INNER JOIN usuarios ON pedidos.id_usuario = usuarios.id_usuario " +
                "INNER JOIN direcciones ON pedidos.id_direccion = direcciones.id_direccion " +
                "INNER JOIN restaurantes ON pedidos.id_restaurante = restaurantes.id_restaurante " +
                "WHERE pedidos.id_pedido = ?;";

        String queryCart = "SELECT detalles_carrito.cantidad, productos.nombre " +
                "FROM detalles_carrito " +
                "INNER JOIN productos ON detalles_carrito.id_producto = productos.id_producto " +
                "WHERE id_carrito = ?";
        String queryReview = "SELECT * FROM valoraciones WHERE id_pedido = ?;";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id_pedido);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Address address = new Address();
                RestaurantResponse restaurantResponse = new RestaurantResponse();
                CartRestaurantResponse cartRestaurantResponse = new CartRestaurantResponse();

                //Crear nueva lista para cada pedido
                List<DetailCartResponse> detailCartResponseList = new ArrayList<>();

                // Obtener el id_carrito y id_pedido del resultado actual
                int id_carrito = rs.getInt("id_carrito");

                // Configurar dirección
                address.setCalle(rs.getString("calle"));
                address.setColonia(rs.getString("colonia"));
                address.setNumero_casa(rs.getString("numero_casa"));
                address.setCodigo_postal(rs.getString("codigo_postal"));
                address.setReferencia(rs.getString("referencia"));

                // Configurar restaurante
                restaurantResponse.setNombre_restaurante(rs.getString("nombre_restaurante"));
                restaurantResponse.setDireccion(rs.getString("direccion"));

                // Configurar orden principal
                orderUserResponse.setId_pedido(rs.getInt("id_pedido"));
                orderUserResponse.setId_status(rs.getInt("id_status"));
                orderUserResponse.setFecha_pedido(rs.getString("fecha_pedido"));
                orderUserResponse.setTotalFinal(rs.getFloat("total"));
                orderUserResponse.setNombre(rs.getString("nombre"));
                orderUserResponse.setTarifa(rs.getInt("tarifa"));
                cartRestaurantResponse.setTotal(rs.getFloat("total"));
                orderUserResponse.setDireccion(address);
                orderUserResponse.setRestaurante(restaurantResponse);

                // Obtener detalles del carrito
                try (Connection conn2 = DatabaseConfig.getDataSource().getConnection();
                     PreparedStatement stmt2 = conn2.prepareStatement(queryCart)) {

                    stmt2.setInt(1, id_carrito);
                    ResultSet rs2 = stmt2.executeQuery();

                    while (rs2.next()) {
                        DetailCartResponse detailCartResponse = new DetailCartResponse();
                        detailCartResponse.setNombre(rs2.getString("nombre"));
                        detailCartResponse.setCantidad(rs2.getInt("cantidad"));
                        detailCartResponseList.add(detailCartResponse);
                    }

                    cartRestaurantResponse.setDetalleCarrito(detailCartResponseList);
                }
                try (Connection conn3 = DatabaseConfig.getDataSource().getConnection();
                     PreparedStatement stmt3 = conn3.prepareStatement(queryReview)) {
                    stmt3.setInt(1, id_pedido);
                    ResultSet rs3 = stmt3.executeQuery();
                    if (rs3.next()) {
                        orderUserResponse.setPuntuacion_pedido(rs3.getInt("puntuacion_pedido"));
                    }

                }
                orderUserResponse.setCarrito(cartRestaurantResponse);
            }
        }

        return orderUserResponse;
    }
}
