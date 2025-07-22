package org.alilopez.repository;

import org.alilopez.config.DatabaseConfig;
import org.alilopez.model.Transport;

import java.sql.*;

public class TransportRepository {

    public Transport getTransportByUserId(int id_usuario)  throws SQLException {
        Transport transport = null;
        String query = "SELECT * FROM transporte WHERE id_usuario = ?";

        try(Connection conn = DatabaseConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1,id_usuario);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                transport = new Transport();
                transport.setId_trasport(rs.getInt("id_transporte"));
                transport.setId_usuario(rs.getInt("id_usuario"));
                transport.setId_vehiculo(rs.getInt("id_vehiculo"));
                transport.setPlacas(rs.getString("placas"));
                transport.setUrl_foto_vehicle(rs.getString("url_foto_vehiculo"));
            }
        }
        return transport;
    }

    public void deleteTransport(int id) throws SQLException {
        String query = "DELETE FROM transporte WHERE id_transporte = ?";

        try(Connection conn = DatabaseConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public int saveTransport(Transport transport) throws SQLException {
        int id=0;
        String query = "INSERT INTO transporte (id_vehiculo, id_usuario, placas, url_foto_vehiculo) VALUES (?, ?, ?, ?)";
        try(Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            stmt.setInt(1, transport.getId_vehiculo());
            stmt.setInt(2, transport.getId_usuario());
            stmt.setString(3, transport.getPlacas());
            stmt.setString(4, transport.getUrl_foto_vehicle());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
        }
        return id;
    }

    public int updateTransport(int id_transporte, Transport transport) throws SQLException {
        int id=0;
        String query = "UPDATE transporte SET id_vehiculo = ?, id_usuario = ?, placas = ?, url_foto_vehiculo = ? WHERE id_transporte = ?";
        try(Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            stmt.setInt(1, transport.getId_vehiculo());
            stmt.setInt(2, transport.getId_usuario());
            stmt.setString(3, transport.getPlacas());
            stmt.setString(4, transport.getUrl_foto_vehicle());
            stmt.setInt(5, id_transporte);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
        }
        return id;
    }
}
