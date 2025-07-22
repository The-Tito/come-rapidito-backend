package org.alilopez.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.alilopez.DTO.ProductDTO;
import org.alilopez.model.Product;
import org.alilopez.service.ProductService;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public void getAllProducts(Context ctx) throws SQLException {
        try {
            List<Product> products = productService.getAllProducts();
            ctx.status(200).json(products);
        }catch (SQLException e) {
            ctx.status(500).result("Error al obtener productos");
            e.printStackTrace();
        }
    }

    public void getProductByIdRestaurante(Context ctx) throws SQLException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id_restaurante"));
            List<ProductDTO> products = productService.getByIdPRestaurante(id);
            if (products != null) {
                ctx.status(200).json(products);
            } else {
                ctx.status(HttpStatus.BAD_REQUEST).result("Error al obtener productos");
            }
        } catch (Exception e) {
            ctx.status(404).result("Error al obtener productos");
            e.printStackTrace();
        }
        }

    public void deleteProduct(Context ctx) throws SQLException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            productService.deleteProduct(id);
            ctx.status(HttpStatus.OK).result("Producto eliminado");
        }catch (Exception e){
            ctx.status(HttpStatus.BAD_REQUEST).result("Error al eliminar producto");

        }
    }

    }



