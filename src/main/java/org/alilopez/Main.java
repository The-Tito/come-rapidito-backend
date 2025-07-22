package org.alilopez;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.alilopez.di.AppModule;

public class Main {
    public static void main(String[] args) {;
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        }).start(7000);



        // Ruta raíz (pública)
        app.get("/", ctx -> ctx.result("API Javalin 2"));


        // Rutas públicas (sin autenticación)
        AppModule.initUser().publicRoutes(app);
        AppModule.initRestaurante().publicRoutes(app);
        AppModule.initProducts().publicRoutes(app);
        AppModule.initStats().publicRoutes(app);
        // Middleware de autenticación para todas las rutas /api/*
        app.before("/api/*", ctx -> {
            // Permitir peticiones OPTIONS (preflight) sin autenticación
            if (!"OPTIONS".equals(ctx.method().toString())) {
                AppModule.initUser().authorize(ctx);
            }
        });
        // Rutas protegidas (requieren autenticación)
        AppModule.initUser().register(app);
        AppModule.initProducts().products(app);
        AppModule.initRestaurante().Restaurante(app);
        AppModule.initAddress().address(app);
        AppModule.initTransport().transport(app);
        AppModule.initOrder().orders(app);
        AppModule.initReview().Reviews(app);
    }
}