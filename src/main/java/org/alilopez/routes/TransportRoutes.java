package org.alilopez.routes;

import io.javalin.Javalin;
import org.alilopez.controller.ImageController;
import org.alilopez.controller.TransportController;

public class TransportRoutes {
    private TransportController transportController;
    private ImageController imageController;
    public TransportRoutes(TransportController transportController, ImageController imageController) {
        this.transportController = transportController;
        this.imageController = imageController;
    }

    public void transport(Javalin app) {
        app.get("/api/transport/{id_usuario}", transportController::getTransportByUserId);
        app.post("/api/transport", imageController::createTransport);
        app.put("/api/transport/{id}", imageController::updateTransport);
        app.delete("/api/transport/{id}", transportController::deleteTransport);
    }


}
