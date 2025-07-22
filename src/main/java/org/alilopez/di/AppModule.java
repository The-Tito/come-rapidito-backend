package org.alilopez.di;

import org.alilopez.controller.*;
import org.alilopez.managers.JwtTokenManagerImpl;
import org.alilopez.managers.TokenManager;
import org.alilopez.model.Restaurante;
import org.alilopez.repository.*;
import org.alilopez.routes.*;
import org.alilopez.service.*;

public class AppModule {
    public static UserRoutes initUser() {
        TokenManager manager = new JwtTokenManagerImpl() {};
        UserRepository userRepo = new UserRepository();
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepo, manager);
        UserService userService = new UserServiceImpl(userRepo, manager);
        UserController userController = new UserController(userServiceImpl, userService);
        return new UserRoutes(userController);
    }

    public static ProductRoutes initProducts() {
        ProductRepository productRepository = new ProductRepository();
        ProductService productService = new ProductService(productRepository);
        ProductController productController = new ProductController(productService);

        // Dependencias para ImageController
        CloudinaryService cloudinaryService = new CloudinaryService();
        ImageController imageController = new ImageController(cloudinaryService, productRepository);

        return new ProductRoutes(productController, imageController);
    }

    public static RestauranteRoutes initRestaurante() {
        RestauranteRepository restauranteRepository = new RestauranteRepository();
        RestauranteService restauranteService = new RestauranteService(restauranteRepository);
        RestauranteController restauranteController = new RestauranteController(restauranteService);

        CloudinaryService cloudinaryService = new CloudinaryService();
        ImageController imageController = new ImageController(cloudinaryService, restauranteRepository);
        return new RestauranteRoutes(restauranteController, imageController);
    }

    public static AddressRoutes initAddress(){
        AddressRepository addressRepository = new AddressRepository();
        AddressService addressService = new AddressService(addressRepository);
        AddressController addressController = new AddressController(addressService);
        return new AddressRoutes(addressController);
    }

    public static TransportRoutes initTransport(){
        TransportRepository transportRepository = new TransportRepository();
        TransportService transportService = new TransportService(transportRepository);
        TransportController transportController = new TransportController(transportService);

        CloudinaryService cloudinaryService = new CloudinaryService();
        ImageController imageController = new ImageController(cloudinaryService, transportRepository);
        return new TransportRoutes(transportController, imageController);
    }

    public static OrderRoutes initOrder(){
        OrderRepository orderRepository = new OrderRepository();
        CartRepository cartRepository = new CartRepository();
        DetailCartRepository detailCartRepository = new DetailCartRepository();
        OrderService orderService = new OrderService(orderRepository,  cartRepository, detailCartRepository);
        OrderController orderController = new OrderController(orderService);
        return new OrderRoutes(orderController);
    }

    public static  ReviewRoutes initReview(){
        ReviewRepository reviewRepository = new ReviewRepository();
        ReviewService reviewService = new ReviewService(reviewRepository);
        ReviewController reviewController = new ReviewController(reviewService);
        return new ReviewRoutes(reviewController);
    }
    public static StatsRoutes initStats() {
        StatsRepository  statsRepository = new StatsRepository();
        StatsService statsService = new StatsService(statsRepository);
        StatsController  statsController = new StatsController(statsService);
        StatsRoutes statsRoutes = new StatsRoutes(statsController);
        return statsRoutes;
    }
}