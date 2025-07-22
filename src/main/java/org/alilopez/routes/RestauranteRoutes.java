package org.alilopez.routes;

import io.javalin.Javalin;
import org.alilopez.controller.ImageController;
import org.alilopez.controller.RestauranteController;

public class RestauranteRoutes {
    private RestauranteController restauranteController;
    private ImageController imageController;

    public RestauranteRoutes(RestauranteController restauranteController, ImageController imageController) {
        this.restauranteController = restauranteController;
        this.imageController = imageController;
    }

    public void publicRoutes (Javalin app){
        app.get("/restaurant", restauranteController::getRestaurantes);
    }

    public void Restaurante(Javalin app) {
        app.get("/api/restaurant/{id_usuario}", restauranteController::getRestauranteByUserId);
        app.post("/api/restaurant", imageController::createRestaurante);
        app.put("/api/restaurant/{id}", imageController::updateRestaurante);
        app.delete("/api/restaurant/{id}", restauranteController::deleteRestaurante);



    }
}
