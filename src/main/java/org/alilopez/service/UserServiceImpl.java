package org.alilopez.service;

import java.util.Optional;

import org.alilopez.model.AuthRequest;
import org.alilopez.model.AuthResponse;
import org.alilopez.model.User;
import org.alilopez.managers.TokenManager;
import org.alilopez.repository.UserRepository;
import io.javalin.http.ForbiddenResponse;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements  UserService {
    private UserRepository userRepo;
    private TokenManager manager;
    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    public UserServiceImpl(UserRepository userRepo, TokenManager manager) {
        this.userRepo = userRepo;
        this.manager = manager;
    }


    public List<User> getAllUsers() throws SQLException {
        return userRepo.findAll();
    }

    public User getByIdUser(int idUser) throws SQLException {
        return userRepo.findByIdUser(idUser);
    }

    public void deleteUser(int id_usuario) throws SQLException {
        userRepo.deleteUser(id_usuario);
    }

    public void updateUser(int id_usuario, User user) throws SQLException {
        userRepo.updateUser(id_usuario, user);
    }

    @Override
    public AuthResponse signup(User user) throws SQLException {
        int id_usuario = userRepo.save(user);
        String nombre = user.getNombre();
        String token = manager.issueToken(nombre);
        int idRol = user.getIdRol();
        AuthResponse response = new AuthResponse(nombre, token, idRol, id_usuario);
        return response;
    }

    @Override
    public AuthResponse login(AuthRequest request) throws SQLException {
        String email = request.getCorreo_electronico();
        String numero_telefono = request.getNumero_telefono();
        String password = request.getContrasena();
        User result = userRepo.login(email, numero_telefono, password);
        String nombre = result.getNombre();
        String token = manager.issueToken(nombre);
        int id_usuario = result.getId_usuario();
        System.out.println(id_usuario);
        int idRol = result.getIdRol();
        AuthResponse response = new AuthResponse(nombre, token, idRol,  id_usuario);
        return response;
    }


    @Override
    public boolean authorize(String token, String nombre) {
        boolean result = manager.authorize(token, nombre);

        return result;

    }
}
