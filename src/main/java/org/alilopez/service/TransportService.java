package org.alilopez.service;

import org.alilopez.model.Transport;
import org.alilopez.repository.TransportRepository;

import java.sql.SQLException;

public class TransportService {
    private TransportRepository transportRepository;
    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    public Transport getTransportByUserId(int id_usuario)  throws SQLException {
        return transportRepository.getTransportByUserId(id_usuario);
    }

    public void deleteTransport(int id) throws SQLException {
        transportRepository.deleteTransport(id);
    }
}
