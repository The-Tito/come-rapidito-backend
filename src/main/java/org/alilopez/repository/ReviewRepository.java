package org.alilopez.repository;

import org.alilopez.config.DatabaseConfig;
import org.alilopez.model.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReviewRepository {

    public void save(Review review) throws SQLException {
        String query = "INSERT INTO valoraciones (id_pedido, puntuacion_pedido) VALUES (?,?)";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, review.getId_pedido());
            stmt.setInt(2, review.getPuntuacion());
            stmt.executeUpdate();
        }
    }


}
