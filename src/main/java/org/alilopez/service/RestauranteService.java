package org.alilopez.service;

import org.alilopez.model.Restaurante;
import org.alilopez.repository.RestauranteRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class RestauranteService {
    RestauranteRepository restauranteRepository ;
    public RestauranteService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public ArrayList<Restaurante> findAllRestaurantes() throws SQLException {
        return restauranteRepository.getRestaurantes();
    }

    public Restaurante getRestaurante(int id_usuario) throws SQLException {
        return restauranteRepository.findRestaurantByUserId(id_usuario);
    }

    public void deleteRestaurante(int id) throws SQLException {
        restauranteRepository.deleteRestaurante(id);
    }
}
