package org.alilopez.repository;

import org.alilopez.config.DatabaseConfig;
import org.alilopez.model.DetailCart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetailCartRepository {

    public void save(DetailCart detailCart) throws SQLException {
        DetailCart responseDetailCart = null;
        String query = "INSERT INTO detalles_carrito (id_carrito, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, detailCart.getId_carrito());
            stmt.setInt(2, detailCart.getId_producto());
            stmt.setInt(3, detailCart.getCantidad());
            stmt.setDouble(4, detailCart.getPrecio_unitario());
            stmt.executeUpdate();

        }

    }
}
