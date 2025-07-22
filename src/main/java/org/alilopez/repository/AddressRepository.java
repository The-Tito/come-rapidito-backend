package org.alilopez.repository;

import org.alilopez.config.DatabaseConfig;
import org.alilopez.model.Address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressRepository {

    public Address save(Address address) throws SQLException {
        int id_direccion = 0;
        String query = "INSERT INTO direcciones (id_usuario, calle, colonia, numero_casa, codigo_postal, referencia) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, address.getId_usuario());
            stmt.setString(2, address.getCalle());
            stmt.setString(3, address.getColonia());
            stmt.setString(4, address.getNumero_casa());
            stmt.setString(5, address.getCodigo_postal());
            stmt.setString(6, address.getReferencia());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id_direccion = rs.getInt(1);
            }
            address.setId_direccion(id_direccion);
        return address;
    }
}
    public List<Address> getMyAddresses(int id_usuario) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String query = "SELECT id_direccion, id_usuario, calle, colonia, numero_casa, codigo_postal, referencia " +
                "FROM direcciones WHERE id_usuario = ?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id_usuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Address address = new Address();
                    address.setId_direccion(rs.getInt("id_direccion"));
                    address.setId_usuario(rs.getInt("id_usuario"));
                    address.setCalle(rs.getString("calle"));
                    address.setColonia(rs.getString("colonia"));
                    address.setNumero_casa(rs.getString("numero_casa"));
                    address.setCodigo_postal(rs.getString("codigo_postal"));
                    address.setReferencia(rs.getString("referencia"));
                    addresses.add(address);
                }
            }
        }
        return addresses;
    }

    public Address findById(int id_direccion) throws SQLException {
        Address address = null;
        String query = "SELECT id_direccion, id_usuario, calle, colonia, numero_casa, codigo_postal, referencia " +
                "FROM direcciones WHERE id_direccion = ?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id_direccion);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    address = new Address();
                    address.setId_direccion(rs.getInt("id_direccion"));
                    address.setId_usuario(rs.getInt("id_usuario"));
                    address.setCalle(rs.getString("calle"));
                    address.setColonia(rs.getString("colonia"));
                    address.setNumero_casa(rs.getString("numero_casa"));
                    address.setCodigo_postal(rs.getString("codigo_postal"));
                    address.setReferencia(rs.getString("referencia"));
                }
            }
        }
        return address;
    }

    public void updateAddress(int id_address, Address address) throws SQLException {
        String query = "UPDATE direcciones SET calle = ?, colonia = ?, numero_casa = ?, codigo_postal = ?, referencia = ? where id_direccion = ?";

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, address.getCalle());
            stmt.setString(2, address.getColonia());
            stmt.setString(3, address.getNumero_casa());
            stmt.setString(4, address.getCodigo_postal());
            stmt.setString(5, address.getReferencia());
            stmt.setInt(6, id_address );
            stmt.executeUpdate();

        }
    }

    public void deleteAddress(int id_address) throws SQLException {
        String query = "DELETE FROM direcciones WHERE id_direccion = ?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id_address);
            stmt.executeUpdate();
        }
    }
}
