package org.alilopez.repository;

import org.alilopez.config.DatabaseConfig;
import org.alilopez.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM usuarios";
        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setCorreo_electronico(rs.getString("correo_electronico"));
                u.setNumero_telefono(rs.getString("numero_telefono"));
                users.add(u);
            }
        }
        return users;
    }

    public User findByIdUser(int idUser) throws SQLException {
        User user = null;
        String query = "SELECT * FROM usuarios WHERE id_usuario = ?";

        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUser);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId_usuario(rs.getInt("id_usuario"));
                    user.setNombre(rs.getString("nombre"));
                    user.setApellido(rs.getString("apellido"));
                    user.setCorreo_electronico(rs.getString("correo_electronico"));
                    user.setNumero_telefono(rs.getString("numero_telefono"));
                }
            }
        }

        return user;
    }

    public int save(User user) throws SQLException {
        String checkQuery = "SELECT * FROM usuarios WHERE correo_electronico = ? OR numero_telefono = ?";

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getCorreo_electronico());
            stmt.setString(2, user.getNumero_telefono());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    if (rs.getString("correo_electronico").equals(user.getCorreo_electronico()) ||  rs.getString("numero_telefono").equals(user.getNumero_telefono())) {
                        String erroMSG = "Correo o telefono ya registrado";
                        System.out.println(rs.getString("correo_electronico"));
                        System.out.println(rs.getString("numero_telefono"));
                        throw new SQLException(erroMSG);
                    }
                }
            }
        }

        String query = "INSERT INTO usuarios (nombre, apellido, contrasena, correo_electronico, numero_telefono, id_rol) VALUES (?, ?, ?, ?, ?, ?)";
            int id = 0;
        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getApellido());
            String hashedPassword = BCrypt.hashpw(user.getContrasena(), BCrypt.gensalt());
            stmt.setString(3, hashedPassword);
            stmt.setString(4, user.getCorreo_electronico());
            stmt.setString(5, user.getNumero_telefono());
            stmt.setInt(6, user.getIdRol());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

        }
        return id;
    }

    public void updateUser(int id_usuario, User user) throws SQLException {
        String query = "UPDATE usuarios SET nombre = ?, apellido = ?, correo_electronico = ?, numero_telefono = ?  where id_usuario = ?";
        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, user.getNombre());
                stmt.setString(2, user.getApellido());
                stmt.setString(3, user.getCorreo_electronico());
                stmt.setString(4, user.getNumero_telefono());
                stmt.setInt(5, id_usuario);
                stmt.executeUpdate();

        }

    }

    public void deleteUser(int id_usuario) throws SQLException {
        String query = "Delete FROM usuarios WHERE id_usuario = ?";
        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id_usuario);
            stmt.executeUpdate();
        }
    }

    public User login(String email, String numero_telefono, String password) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE correo_electronico = ? OR numero_telefono = ?";

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) { // Quitar Statement.RETURN_GENERATED_KEYS

            stmt.setString(1, email);
            stmt.setString(2, numero_telefono);

            try (ResultSet rs = stmt.executeQuery()) { // Usar directamente executeQuery()
                if (rs.next()) {
                    // Usuario encontrado - crear objeto User
                    User user = new User();
                    user.setId_usuario(rs.getInt("id_usuario")); // Usar el nombre de la columna
                    user.setNombre(rs.getString("nombre"));
                    user.setApellido(rs.getString("apellido"));
                    user.setCorreo_electronico(rs.getString("correo_electronico"));
                    user.setNumero_telefono(rs.getString("numero_telefono"));
                    user.setIdRol(rs.getInt("id_rol"));

                    // Obtener contraseña hasheada de la BD
                    String hashedPasswordFromDB = rs.getString("contrasena");

                    // Verificar contraseña usando BCrypt
                    if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
                        // Contraseña correcta - retornar usuario
                        user.setContrasena(hashedPasswordFromDB); // opcional, si necesitas el hash
                        return user;
                    } else {
                        // Contraseña incorrecta
                        throw new SQLException("Contraseña incorrecta");
                    }
                } else {
                    // Usuario no encontrado
                    throw new SQLException("Usuario no encontrado con ese correo o teléfono");
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error al realizar login: " + e.getMessage());
        }
    }



}
