package org.alilopez.repository;

import org.alilopez.config.DatabaseConfig;
import org.alilopez.model.Stat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatsRepository {
    public List<Stat> createStatsSum() throws SQLException {
        List<Stat> stats = new ArrayList<>();
        String query = "SELECT SUM(detalles_carrito.cantidad) AS total_ventas, productos.nombre\n" +
                "FROM detalles_carrito\n" +
                "JOIN productos\n" +
                "ON detalles_carrito.id_producto = productos.id_producto\n" +
                "JOIN restaurantes\n" +
                "ON productos.id_restaurante = restaurantes.id_restaurante\n" +
                "GROUP BY productos.id_producto, productos.nombre, restaurantes.nombre_restaurante\n" +
                "ORDER BY total_ventas DESC;";
        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Stat s = new Stat();
                s.setLabel(rs.getString("nombre"));
                s.setValue(rs.getInt("total_ventas"));
                stats.add(s);
            }
        }
        return stats;
    }
}
