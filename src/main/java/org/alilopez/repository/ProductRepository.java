package org.alilopez.repository;

import org.alilopez.DTO.ProductDTO;
import org.alilopez.config.DatabaseConfig;
import org.alilopez.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT productos.id_producto, productos.nombre, productos.id_categoria, productos.precio, productos.descripcion, productos.url_imagen, productos.id_restaurante, productos.id_status  FROM productos;";

        try(
                Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()
                ){
            while (rs.next()) {
                Product p = new Product();
                p.setId_producto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setUrl_imagen(rs.getString("url_imagen"));
                p.setId_categoria(rs.getInt("id_categoria"));
                p.setId_restaurante(rs.getInt("id_restaurante"));
                p.setPrecio(rs.getFloat("precio"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setId_status(rs.getInt("id_status"));
                products.add(p);
            }
        }
        return products;
        }

    public List<ProductDTO> findByIdRestaurante(int id) throws SQLException {
        ProductDTO p = null;
        List<ProductDTO> products = new ArrayList<>();
        String query = "SELECT productos.id_producto, productos.nombre, productos.id_categoria, productos.precio, productos.descripcion, productos.url_imagen, productos.id_restaurante, productos.id_status  FROM productos\n"+
                "WHERE productos.id_restaurante = ?;";
        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
                stmt.setInt(1, id);

                try(ResultSet rs = stmt.executeQuery()){
                    while (rs.next()) {
                        p = new ProductDTO();
                        p.setId_producto(rs.getInt("id_producto"));
                        p.setNombre(rs.getString("nombre"));
                        p.setUrl_imagen(rs.getString("url_imagen"));
                        p.setId_categoria(rs.getInt("id_categoria"));
                        p.setId_restaurante(rs.getInt("id_restaurante"));
                        p.setId_restaurante(id);
                        p.setPrecio(rs.getFloat("precio"));
                        p.setDescripcion(rs.getString("descripcion"));
                        p.setId_status(rs.getInt("id_status"));
                        products.add(p);
                    }

                }
        }
        return products;
        }

    public Product save(Product product) throws SQLException {
        String query = "INSERT INTO productos (nombre, precio, descripcion, id_categoria, id_restaurante, url_Imagen, id_status) VALUES (?,?,?,?,?,?,?)";
        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getNombre());
            stmt.setFloat(2, product.getPrecio());
            stmt.setString(3, product.getDescripcion());
            stmt.setInt(4,product.getId_categoria());
            stmt.setInt(5,product.getId_restaurante());
            stmt.setString(6,product.getUrl_imagen());
            stmt.setInt(7,product.getId_status());
            stmt.executeUpdate();
        }
        return product;
    }

    public Product updateProduct(int id_product, Product product) throws SQLException {
        String query = "UPDATE productos SET nombre = ?, precio = ?, descripcion = ?, id_categoria = ?, id_restaurante = ?, url_Imagen = ?, id_status = ? WHERE id_producto = ?;";
        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)){
                    stmt.setString(1, product.getNombre());
                    stmt.setFloat(2, product.getPrecio());
                    stmt.setString(3, product.getDescripcion());
                    stmt.setInt(4,product.getId_categoria());
                    stmt.setInt(5,product.getId_restaurante());
                    stmt.setString(6,product.getUrl_imagen());
                    stmt.setInt(7,product.getId_status());
                    stmt.setInt(8,id_product);
                    stmt.executeUpdate();
        }
        return product;
    }

    public void deleteProduct(int id_product) throws SQLException {
        String query = "DELETE FROM productos WHERE id_producto = ?;";
        try (
                Connection conn = DatabaseConfig.getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id_product);
            stmt.executeUpdate();

        }
    }
    }

