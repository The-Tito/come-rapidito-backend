package org.alilopez.routes;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.alilopez.controller.UserController;
import org.alilopez.controller.RestauranteController;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class UserRoutes {
    private final UserController userController;
    public UserRoutes(UserController userController) {
        this.userController = userController;
    }
    public void publicRoutes (Javalin app){
        app.post("/login", userController::login);
        app.post("/signup", userController::signup);
    }

    public void register(Javalin app) {
        app.get("/api/users", userController::getAll);
        app.get("/api/users/{id}", userController::getById);
        app.delete("/api/users/{id}", userController::deleteUser);
        app.put("/api/users/{id}", userController::updateUser);
        // Ejemplo de más rutas:
        // app.put("/users/:id", userController::update);
        // app.delete("/users/:id", userController::delete);
    }

    public void authorize(Context context){

        userController.authorize(context);
    }
}
