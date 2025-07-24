package org.alilopez.repository;

import org.alilopez.config.DatabaseConfig;
import org.alilopez.model.Cart;

import java.sql.*;

public class CartRepository {

    public int createCarrito(Cart cart) throws SQLException {
        int id_carrito = 0;
        String query = "INSERT INTO carritos (id_usuario, total) VALUES (?, ?)";

        try(Connection conn = DatabaseConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cart.getId_usuario());
            stmt.setFloat(2, cart.getTotal());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id_carrito = rs.getInt(1);
            }
        }
        return id_carrito;
    }
}
