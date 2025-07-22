package org.alilopez.routes;

import io.javalin.Javalin;
import org.alilopez.controller.AddressController;

public class AddressRoutes {
    private AddressController addressController;
    public AddressRoutes(AddressController addressController) {
        this.addressController = addressController;
    }

    public void address(Javalin app) {
        app.get("/api/address/{id_usuario}", addressController::getMyAddresses);
        app.get("/api/address/{id}", addressController::getAddressById);
        app.post("/api/address", addressController::createAddress);
        app.put("/api/address/{id}", addressController::updateAddress);
        app.delete("/api/address/{id}", addressController::deleteAddress);
    }
}
