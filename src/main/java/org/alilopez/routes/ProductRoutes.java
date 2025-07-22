package org.alilopez.routes;

import io.javalin.Javalin;
import org.alilopez.controller.ProductController;
import org.alilopez.controller.ImageController;

public class ProductRoutes {
    private ProductController productController;
    private ImageController imageController;

    public ProductRoutes(ProductController productController, ImageController imageController) {
        this.productController = productController;
        this.imageController = imageController;
    }
    public void publicRoutes(Javalin app) {
        app.get("/products", productController::getAllProducts);
        app.get("/products/{id_restaurante}", productController::getProductByIdRestaurante);
    }

    public void products(Javalin app) {

        app.post("/api/products", imageController::createProductWithImage);
        app.put("/api/products/{id}", imageController::updateProduct);
        app.delete("/api/products/{id}", productController::deleteProduct);
    }
}