package org.alilopez.routes;

import io.javalin.Javalin;
import org.alilopez.controller.StatsController;

public class StatsRoutes {
    private final StatsController statsController;
    public StatsRoutes(StatsController statsController) {
        this.statsController = statsController;
    }

    public void publicRoutes (Javalin app) {

        app.get("/stats/sum", statsController::createStatsSum);
    }
}
