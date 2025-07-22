package org.alilopez.service;

import org.alilopez.model.AuthRequest;
import org.alilopez.model.AuthResponse;
import org.alilopez.model.User;

import java.sql.SQLException;

public interface UserService {

    AuthResponse signup (User user) throws SQLException;

    AuthResponse login (AuthRequest request) throws SQLException;

    boolean authorize (String token, String nombre);
}
