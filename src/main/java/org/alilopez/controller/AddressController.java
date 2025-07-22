package org.alilopez.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.alilopez.model.Address;
import org.alilopez.service.AddressService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressController {
    private AddressService addressService;
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    public void getMyAddresses(Context ctx){
        try {
            int id_usuario = Integer.parseInt(ctx.pathParam("id_usuario"));
            List<Address> addresses = addressService.getMyAddresses(id_usuario);
            ctx.status(HttpStatus.OK).json(addresses);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(e.getMessage());
        }
    }

    public void getAddressById(Context ctx){
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Address address = addressService.getAddressById(id);
            ctx.status(HttpStatus.OK).json(address);
        }catch (Exception e){
            ctx.status(HttpStatus.BAD_REQUEST).result("Error al buscar el registro: " + e.getMessage());
        }
    }

    public void createAddress(Context ctx){
        try{
            Address address = ctx.bodyAsClass(Address.class);
            Address createdAddress = addressService.createAddress(address);
            ctx.status(HttpStatus.CREATED).json(createdAddress);
        }catch (Exception e){
            ctx.status(HttpStatus.BAD_REQUEST).result("Error al crear el registro: " + e.getMessage());
        }
    }

    public void updateAddress(Context ctx){
        try{
            int id = Integer.parseInt(ctx.pathParam("id"));
            Address newAddress = ctx.bodyAsClass(Address.class);
            addressService.updateAddress(id, newAddress);
            ctx.status(HttpStatus.OK).json(newAddress);
        }catch (Exception e){
            ctx.status(HttpStatus.BAD_REQUEST).result("Error al actualizar el registro: " + e.getMessage());
        }


    }

    public void deleteAddress(Context ctx){
        try{
            int id = Integer.parseInt(ctx.pathParam("id"));
            addressService.deleteAddress(id);
            ctx.status(HttpStatus.OK).result("Usuario elimiando");
        }catch (Exception e){
            ctx.status(HttpStatus.BAD_REQUEST).result("Error al eliminar el registro: " + e.getMessage());
        }
    }


}
