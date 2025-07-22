package org.alilopez.controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.alilopez.model.Transport;
import org.alilopez.service.TransportService;

public class TransportController {
    private TransportService transportService;
    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    public void getTransportByUserId(Context ctx) {
        try{
            int id_usuario = Integer.parseInt(ctx.pathParam("id_usuario"));
            Transport responseTransport = transportService.getTransportByUserId(id_usuario);
            ctx.status(HttpStatus.OK).json(responseTransport);
        }catch (Exception e){
            ctx.status(HttpStatus.BAD_REQUEST).json("Error al obtener la transport del usuario");
        }
    }

    public void deleteTransport(Context ctx) {
        try{
            int id = Integer.parseInt(ctx.pathParam("id"));
            transportService.deleteTransport(id);
            ctx.status(HttpStatus.OK).json("Se elimino el vehiculo");
        }catch (Exception e){
            ctx.status(HttpStatus.BAD_REQUEST).json("Error al eliminar el vehiculo");
        }
    }

}
