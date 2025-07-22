package org.alilopez.repository;

import org.alilopez.config.DatabaseConfig;
import org.alilopez.model.Restaurante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RestauranteRepository {
    public Restaurante save(Restaurante restaurante) throws SQLException {
        String query = "INSERT INTO restaurantes (logo_url, banner_url, nombre_restaurante, telefono, direccion, horario_apertura, horario_cierre, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection conn = DatabaseConfig.getDataSource().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, restaurante.getLogo_url());
                stmt.setString(2, restaurante.getBanner_url());
                stmt.setString(3, restaurante.getNombre_restaurante());
                stmt.setString(4, restaurante.getTelefono());
                stmt.setString(5, restaurante.getDireccion());
                stmt.setTime(6, restaurante.getHorario_apertura());
                stmt.setTime(7, restaurante.getHorario_cierre());
                stmt.setInt(8, restaurante.getId_usuario());
                stmt.executeUpdate();
            }
        return restaurante;
        }

    public ArrayList<Restaurante> getRestaurantes() throws SQLException {
        String query = "SELECT * FROM restaurantes";
        ArrayList<Restaurante> restaurantes = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Restaurante restaurante = new Restaurante();
                restaurante.setId_restaurante(rs.getInt("id_restaurante"));
                restaurante.setLogo_url(rs.getString("logo_url"));
                restaurante.setBanner_url(rs.getString("banner_url"));
                restaurante.setNombre_restaurante(rs.getString("nombre_restaurante"));
                restaurante.setTelefono(rs.getString("telefono"));
                restaurante.setDireccion(rs.getString("direccion"));
                restaurante.setHorario_apertura(rs.getTime("horario_apertura"));
                restaurante.setHorario_cierre(rs.getTime("horario_cierre"));
                restaurante.setId_usuario(rs.getInt("id_usuario"));
                restaurantes.add(restaurante);
            }
        }
        return restaurantes;
    }

    public Restaurante findRestaurantByUserId(int id_usuario) throws  SQLException {
        Restaurante restaurante = new Restaurante();
        String query = "SELECT restaurantes.id_restaurante, restaurantes.id_usuario, restaurantes.logo_url, restaurantes.banner_url, restaurantes.nombre_restaurante, restaurantes.telefono, restaurantes.direccion, restaurantes.horario_apertura, restaurantes.horario_cierre, usuarios.nombre, usuarios.numero_telefono FROM restaurantes\n" +
                "INNER JOIN usuarios ON restaurantes.id_usuario = usuarios.id_usuario WHERE restaurantes.id_usuario = ?";
        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setInt(1, id_usuario);

            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    restaurante = new Restaurante();
                    restaurante.setId_restaurante(rs.getInt("id_restaurante"));
                    restaurante.setLogo_url(rs.getString("logo_url"));
                    restaurante.setBanner_url(rs.getString("banner_url"));
                    restaurante.setNombre_restaurante(rs.getString("nombre_restaurante"));
                    restaurante.setTelefono(rs.getString("telefono"));
                    restaurante.setDireccion(rs.getString("direccion"));
                    restaurante.setHorario_apertura(rs.getTime("horario_apertura"));
                    restaurante.setHorario_cierre(rs.getTime("horario_cierre"));
                    restaurante.setId_usuario(rs.getInt("id_usuario"));
                    restaurante.setNombre_usuario(rs.getString("nombre"));
                    restaurante.setTelefono_usuario(rs.getString("numero_telefono"));
                }
            }
        }
        return restaurante;
    }
    public Restaurante getRestauranteById(int id_restaurante) throws  SQLException {
        Restaurante restaurante = new Restaurante();
        String query = "SELECT restaurantes.id_restaurante, restaurantes.id_usuario, restaurantes.logo_url, restaurantes.banner_url, restaurantes.nombre_restaurante, restaurantes.telefono, restaurantes.direccion, restaurantes.horario_apertura, restaurantes.horario_cierre, usuarios.nombre, usuarios.numero_telefono FROM restaurantes\n" +
                "INNER JOIN usuarios ON restaurantes.id_usuario = usuarios.id_usuario WHERE restaurantes.id_restaurante = ?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id_restaurante);
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    restaurante = new Restaurante();
                    restaurante.setId_restaurante(rs.getInt("id_restaurante"));
                    restaurante.setLogo_url(rs.getString("logo_url"));
                    restaurante.setBanner_url(rs.getString("banner_url"));
                    restaurante.setNombre_restaurante(rs.getString("nombre_restaurante"));
                    restaurante.setTelefono(rs.getString("telefono"));
                    restaurante.setDireccion(rs.getString("direccion"));
                    restaurante.setHorario_apertura(rs.getTime("horario_apertura"));
                    restaurante.setHorario_cierre(rs.getTime("horario_cierre"));
                    restaurante.setId_usuario(rs.getInt("id_usuario"));
                    restaurante.setNombre_usuario(rs.getString("nombre"));
                    restaurante.setTelefono_usuario(rs.getString("numero_telefono"));

                }
            }
        }
        return restaurante;
    }
    public Restaurante updateRestaurante(int id_restaurante, Restaurante restaurante) throws SQLException {
        // Construir query dinámica basada en si hay nuevas imágenes
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE usuarios u\n")
                .append("JOIN restaurantes r ON u.id_usuario = r.id_usuario\n")
                .append("SET\n")
                .append("  u.nombre = ?,\n")
                .append("  u.numero_telefono = ?,\n");

        // Solo actualizar imágenes si se proporcionan
        boolean hasLogo = restaurante.getLogo_url() != null && !restaurante.getLogo_url().isEmpty();
        boolean hasBanner = restaurante.getBanner_url() != null && !restaurante.getBanner_url().isEmpty();

        if (hasLogo) {
            queryBuilder.append("  r.logo_url = ?,\n");
        }
        if (hasBanner) {
            queryBuilder.append("  r.banner_url = ?,\n");
        }

        queryBuilder.append("  r.nombre_restaurante = ?,\n")
                .append("  r.telefono = ?,\n")
                .append("  r.direccion = ?,\n")
                .append("  r.horario_apertura = ?,\n")
                .append("  r.horario_cierre = ?,\n")
                .append("  r.id_usuario = ?\n")
                .append("WHERE r.id_restaurante = ?");

        String query = queryBuilder.toString();

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;

            // Parámetros básicos
            stmt.setString(paramIndex++, restaurante.getNombre_usuario());
            stmt.setString(paramIndex++, restaurante.getTelefono_usuario());

            // Parámetros condicionales para imágenes
            if (hasLogo) {
                stmt.setString(paramIndex++, restaurante.getLogo_url());
            }
            if (hasBanner) {
                stmt.setString(paramIndex++, restaurante.getBanner_url());
            }

            // Resto de parámetros
            stmt.setString(paramIndex++, restaurante.getNombre_restaurante());
            stmt.setString(paramIndex++, restaurante.getTelefono());
            stmt.setString(paramIndex++, restaurante.getDireccion());
            stmt.setTime(paramIndex++, restaurante.getHorario_apertura());
            stmt.setTime(paramIndex++, restaurante.getHorario_cierre());
            stmt.setInt(paramIndex++, restaurante.getId_usuario());
            stmt.setInt(paramIndex++, id_restaurante);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Filas afectadas en la actualización: " + rowsAffected);
            System.out.println("Query ejecutada: " + query);
            System.out.println("Logo actualizado: " + hasLogo + ", Banner actualizado: " + hasBanner);

            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el restaurante con ID: " + id_restaurante);
            }

            // Asignar el ID al objeto para devolverlo completo
            restaurante.setId_restaurante(id_restaurante);

        } catch (SQLException e) {
            System.err.println("Error en updateRestaurante: " + e.getMessage());
            throw e;
        }

        return restaurante;
    }

    public void deleteRestaurante(int id) throws SQLException {
        String query = "DELETE FROM restaurantes WHERE id_restaurante = ?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

    }
}

