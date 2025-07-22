package org.alilopez.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import me.geso.tinyvalidator.ConstraintViolation;
import org.alilopez.model.AuthRequest;
import org.alilopez.model.AuthResponse;
import org.alilopez.model.User;
import org.alilopez.service.UserService;
import org.alilopez.service.UserServiceImpl;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import io.javalin.http.ForbiddenResponse;

public class UserController {
    private final UserServiceImpl userServiceImpl;
    private UserService userService;

    public UserController(UserServiceImpl userServiceImpl, UserService userService) {
        this.userServiceImpl = userServiceImpl;
        this.userService = userService;
    }

    public void getAll(Context ctx) {
        try {
            List<User> users = userServiceImpl.getAllUsers();
            ctx.status(200).json(users);
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error con la base de datos"+e.getMessage());
        }catch (Exception e){
            ctx.status(HttpStatus.UNAUTHORIZED).result("Error de aurtorizacion "+e.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            User user = userServiceImpl.getByIdUser(id);
            if (user != null) {
                ctx.status(200).json(user);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Usuario no encontrado");
            }
        } catch (Exception e) {
            ctx.status(404).result("Error al obtener usuarios");
        }
    }

    public void updateUser(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            User newUser = ctx.bodyAsClass(User.class);
            userServiceImpl.updateUser(id, newUser);
            ctx.status(200).json(newUser);
        }catch (Exception e){
            ctx.status(HttpStatus.CONFLICT).result(e.getMessage());
        }
    }

    public void deleteUser(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            userServiceImpl.deleteUser(id);
            ctx.status(200).result("Usuario eliminado");
        }catch (Exception e){
            ctx.status(HttpStatus.BAD_REQUEST).result("Error al eliminar usuario");
        }
    }

    public void login(Context ctx) throws SQLException {
        try {
            AuthRequest request = ctx.bodyAsClass(AuthRequest.class);
            AuthResponse result = userServiceImpl.login(request);
            ctx.status(HttpStatus.OK).json(result);
        }catch (Exception e){
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Error con el servidor"+e.getMessage());
        }
    }

    public void signup(Context ctx) {
        try {
            User user = ctx.bodyAsClass(User.class);
            AuthResponse response = userServiceImpl.signup(user);
            ctx.status(201).json(response);
        } catch (Exception e) {
            ctx.status(HttpStatus.CONFLICT).result("Correo o telefono registrados");
        }
    }

    // SOLUCIÓN 1: Lanzar una excepción (Recomendada)
    public void authorize(Context ctx) {


        try {
            String token = ctx.header("Authorization");
            String nombre = ctx.header("X-User-NAME");


            if (token == null || nombre == null) {

                throw new ForbiddenResponse("Token de autorización requerido");
            }

            // Corregir encoding UTF-8 si es necesario
            if (nombre != null) {
                try {
                    nombre = new String(nombre.getBytes("ISO-8859-1"), "UTF-8");
                } catch (Exception e) {
                    // Si falla, usar el nombre original
                }
            }

            boolean isAuthorized = userService.authorize(token, nombre);

            if (isAuthorized) {

            } else {


                throw new ForbiddenResponse("No autorizado");
            }

        } catch (ForbiddenResponse e) {
            // Re-lanzar excepciones de autorización
            throw e;
        } catch (Exception e) {
            System.out.println("Error de autorización: " + e.getMessage());

            throw new ForbiddenResponse("Error de autorización: " + e.getMessage());
        }
    }
}