package org.alilopez.routes;

import io.javalin.Javalin;
import org.alilopez.controller.OrderController;

public class OrderRoutes {
    private OrderController orderController;
    public OrderRoutes(OrderController orderController) {
        this.orderController = orderController;
    }

    public void orders(Javalin app) {
        app.post("/api/orders", orderController::createOrder);
        app.put("/api/orders/{id}/status", orderController::updateStatus);
        app.put("/api/orders/{id}/fee", orderController::updateFee);
        app.get("/api/orders/restaurant/{id}", orderController::ativeOrderByRestaurantId);
        app.get("/api/orders/restaurant/{id}/history", orderController::historyOrdersRestaurant);
        app.get("/api/orders/delivery", orderController::getActiveOrders);
        app.get("/api/orders/delivery/history", orderController::getHistory);
        app.get("/api/users/{id}/orders", orderController::historyOrdersByIdUser);
        app.get("/api/users/{id}/orders/active", orderController::activeOrdersByIdUser);
        app.get("/api/users/{id}/orders/actual", orderController::actualOrder);
    }

}
