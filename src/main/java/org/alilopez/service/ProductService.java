package org.alilopez.service;

import org.alilopez.DTO.ProductDTO;
import org.alilopez.model.Product;
import org.alilopez.repository.ProductRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getAllProducts() throws SQLException {
        return productRepository.findAll();
    }

    public List<ProductDTO> getByIdPRestaurante(int id) throws SQLException {
        return productRepository.findByIdRestaurante(id);
    }

    public void deleteProduct(int idProduct) throws SQLException {
        productRepository.deleteProduct(idProduct);
    }
}
