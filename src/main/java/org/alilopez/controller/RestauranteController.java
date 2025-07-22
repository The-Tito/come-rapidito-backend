package org.alilopez.controller;

import io.javalin.Javalin;
import io.javalin.http.Context;


import io.javalin.http.HttpStatus;
import org.alilopez.model.Restaurante;
import org.alilopez.service.RestauranteService;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class RestauranteController {
    RestauranteService restauranteService ;
    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    public void getRestaurantes(Context ctx) {
        try {
            ArrayList<Restaurante> restaurantes = new ArrayList<>();
            restaurantes = restauranteService.findAllRestaurantes();
            ctx.status(HttpStatus.OK).json(restaurantes);
        }catch (Exception e){
            ctx.status(HttpStatus.CONFLICT).json("Error al obtener el restaurantes");
        }
    }

    public void getRestauranteByUserId(Context ctx) throws SQLException {
        try {
            int id_usuario = Integer.parseInt(ctx.pathParam("id_usuario"));
            Restaurante restaurante = restauranteService.getRestaurante(id_usuario);
            ctx.status(200).json(restaurante);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteRestaurante(Context ctx) throws SQLException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            restauranteService.deleteRestaurante(id);
            ctx.status(HttpStatus.OK).json("Restaurante eliminado");
        }catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json("Error al eliminar restaurante");
        }
    }

}
