package org.alilopez.controller;

import io.javalin.http.HttpStatus;
import org.alilopez.model.Product;
import org.alilopez.model.Restaurante;
import org.alilopez.model.Transport;
import org.alilopez.repository.ProductRepository;
import org.alilopez.repository.RestauranteRepository;
import org.alilopez.repository.TransportRepository;
import org.alilopez.service.CloudinaryService;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImageController {
    private CloudinaryService cloudinaryService;
    private ProductRepository productRepository;
    private RestauranteRepository restauranteRepository;
    private TransportRepository transportRepository;

    public ImageController(CloudinaryService cloudinaryService, ProductRepository productRepository) {
        this.cloudinaryService = cloudinaryService;
        this.productRepository = productRepository;
    }

    public ImageController(CloudinaryService cloudinaryService, RestauranteRepository restauranteRepository) {
        this.cloudinaryService = cloudinaryService;
        this.restauranteRepository = restauranteRepository;
    }

    public ImageController(CloudinaryService cloudinaryService, TransportRepository transportRepository, RestauranteRepository restauranteRepository) {
        this.cloudinaryService = cloudinaryService;
        this.transportRepository = transportRepository;
        this.restauranteRepository = restauranteRepository;
    }

    public ImageController(CloudinaryService cloudinaryService, TransportRepository transportRepository) {
        this.cloudinaryService = cloudinaryService;
        this.transportRepository = transportRepository;
    }


    public void createProductWithImage(Context ctx) throws IOException {
        String publicId = null;

        try {
            // Validar que la request sea multipart
            if (!ctx.isMultipart()) {
                ctx.status(400).json(Map.of("error", "Request debe ser multipart/form-data"));
                return;
            }

            // Obtener parámetros del formulario
            String nombre = ctx.formParam("nombre");
            String precio = ctx.formParam("precio");
            String descripcion = ctx.formParam("descripcion");
            String id_categoria = ctx.formParam("id_categoria");
            String id_restaurante = ctx.formParam("id_restaurante");
            String id_status = ctx.formParam("id_status");

            // Validaciones básicas
            if (nombre == null || nombre.trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "El nombre es requerido"));
                return;
            }

            if (precio == null || precio.trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "El precio es requerido"));
                return;
            }

            if (id_categoria == null || id_restaurante == null) {
                ctx.status(400).json(Map.of("error", "Categoría y restaurante son requeridos"));
                return;
            }

            // Parsear valores numéricos
            float precioFloat;
            int id_categoriaInteger;
            int id_restauranteInteger;
            int  id_statusInteger;
            try {
                precioFloat = Float.parseFloat(precio);
                id_categoriaInteger = Integer.parseInt(id_categoria);
                id_restauranteInteger = Integer.parseInt(id_restaurante);
                id_statusInteger = Integer.parseInt(id_status);
            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of("error", "Formato numérico inválido"));
                e.printStackTrace();
                return;
            }

            if (precioFloat <= 0) {
                ctx.status(400).json(Map.of("error", "El precio debe ser mayor a 0"));
                return;
            }

            // Procesar imagen (opcional)
            String urlImagen = null;
            UploadedFile file = ctx.uploadedFile("imagen");

            if (file != null) {
                // Validar tipo de archivo
                String contentType = file.contentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    ctx.status(400).json(Map.of("error", "El archivo debe ser una imagen"));
                    return;
                }

                // Validar tamaño de archivo (ejemplo: máximo 5MB)
                if (file.size() > 5 * 1024 * 1024) {
                    ctx.status(400).json(Map.of("error", "El archivo es demasiado grande (máximo 5MB)"));
                    return;
                }

                // Generar ID único para la imagen
                publicId = "product_" + UUID.randomUUID().toString();

                // Subir a Cloudinary
                try (InputStream imageStream = file.content();){
                    byte[] imageBytes = imageStream.readAllBytes();
                    urlImagen = cloudinaryService.uploadImage(imageBytes, publicId);
                } catch (IOException e) {
                    ctx.status(500).json(Map.of("error", "Error al procesar la imagen: " + e.getMessage()));
                    return;
                }
            }

            // Crear objeto producto
            Product product = new Product();
            product.setNombre(nombre.trim());
            product.setPrecio(precioFloat);
            product.setDescripcion(descripcion != null ? descripcion.trim() : null);
            product.setId_categoria(id_categoriaInteger);
            product.setId_restaurante(id_restauranteInteger);
            product.setId_status(id_statusInteger);
            product.setUrl_imagen(urlImagen);

            // Guardar en base de datos
            Product savedProduct = productRepository.save(product);

            // Respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto creado exitosamente");
            response.put("product", Map.of(
                    "id", savedProduct.getId_producto(),
                    "nombre", savedProduct.getNombre(),
                    "precio", savedProduct.getPrecio(),
                    "descripcion", savedProduct.getDescripcion() != null ? savedProduct.getDescripcion() : "",
                    "id_categoria", savedProduct.getId_categoria(),
                    "id_estaurante", savedProduct.getId_restaurante(),
                    "url_Imagen", savedProduct.getUrl_imagen() != null ? savedProduct.getUrl_imagen() : ""));

            ctx.status(200).json(response);

        } catch (Exception e) {
            e.printStackTrace();

            // Si hay error y se subió imagen, eliminarla de Cloudinary
            if (publicId != null) {
                try {
                    cloudinaryService.deleteImage(publicId);
                } catch (Exception deleteError) {
                    System.err.println("Error al eliminar imagen de Cloudinary: " + deleteError.getMessage());
                }
            }

            ctx.status(500).json(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    public void updateProduct(Context ctx) throws  SQLException {
        String publicId = null;

        try {
            // Validar que la request sea multipart
            if (!ctx.isMultipart()) {
                ctx.status(400).json(Map.of("error", "Request debe ser multipart/form-data"));
                return;
            }

            // Obtener parámetros del formulario
            int id_product = Integer.parseInt(ctx.pathParam("id"));
            String nombre = ctx.formParam("nombre");
            String precio = ctx.formParam("precio");
            String descripcion = ctx.formParam("descripcion");
            String id_categoria = ctx.formParam("id_categoria");
            String id_restaurante = ctx.formParam("id_restaurante");
            String id_status = ctx.formParam("id_status");

            // Validaciones básicas
            if (nombre == null || nombre.trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "El nombre es requerido"));
                return;
            }

            if (precio == null || precio.trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "El precio es requerido"));
                return;
            }

            if (id_categoria == null || id_restaurante == null) {
                ctx.status(400).json(Map.of("error", "Categoría y restaurante son requeridos"));
                return;
            }

            // Parsear valores numéricos
            float precioFloat;
            int id_categoriaInteger;
            int id_restauranteInteger;
            int id_statusInteger;
            try {
                precioFloat = Float.parseFloat(precio);
                id_categoriaInteger = Integer.parseInt(id_categoria);
                id_restauranteInteger = Integer.parseInt(id_restaurante);
                id_statusInteger = Integer.parseInt(id_status);
            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of("error", "Formato numérico inválido"));
                return;
            }

            if (precioFloat <= 0) {
                ctx.status(400).json(Map.of("error", "El precio debe ser mayor a 0"));
                return;
            }

            // Procesar imagen (opcional)
            String urlImagen = null;
            UploadedFile file = ctx.uploadedFile("imagen");

            if (file != null) {
                // Validar tipo de archivo
                String contentType = file.contentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    ctx.status(400).json(Map.of("error", "El archivo debe ser una imagen"));
                    return;
                }

                // Validar tamaño de archivo (ejemplo: máximo 5MB)
                if (file.size() > 5 * 1024 * 1024) {
                    ctx.status(400).json(Map.of("error", "El archivo es demasiado grande (máximo 5MB)"));
                    return;
                }

                // Generar ID único para la imagen
                publicId = "product_" + UUID.randomUUID().toString();

                // Subir a Cloudinary
                try {
                    byte[] imageBytes = file.content().readAllBytes();
                    urlImagen = cloudinaryService.uploadImage(imageBytes, publicId);
                } catch (IOException e) {
                    ctx.status(500).json(Map.of("error", "Error al procesar la imagen: " + e.getMessage()));
                    return;
                }
            }

            // Crear objeto producto
            Product product = new Product();
            product.setNombre(nombre.trim());
            product.setPrecio(precioFloat);
            product.setDescripcion(descripcion != null ? descripcion.trim() : null);
            product.setId_categoria(id_categoriaInteger);
            product.setId_restaurante(id_restauranteInteger);
            product.setId_status(id_statusInteger);
            product.setUrl_imagen(urlImagen);

            // Guardar en base de datos
            Product savedProduct = productRepository.updateProduct(id_product, product);

            // Respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto actualizado exitosa.......................................................mente");
            response.put("Producto", Map.of(
                    "id", savedProduct.getId_producto(),
                    "nombre", savedProduct.getNombre(),
                    "precio", savedProduct.getPrecio(),
                    "descripcion", savedProduct.getDescripcion() != null ? savedProduct.getDescripcion() : "",
                    "id_categoria", savedProduct.getId_categoria(),
                    "id_estaurante", savedProduct.getId_restaurante(),
                    "url_Imagen", savedProduct.getUrl_imagen() != null ? savedProduct.getUrl_imagen() : ""));

            ctx.status(200).json(response);

        } catch (Exception e) {
            e.printStackTrace();

            // Si hay error y se subió imagen, eliminarla de Cloudinary
            if (publicId != null) {
                try {
                    cloudinaryService.deleteImage(publicId);
                } catch (Exception deleteError) {
                    System.err.println("Error al eliminar imagen de Cloudinary: " + deleteError.getMessage());
                }
            }

            ctx.status(500).json(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    public void createRestaurante(Context ctx) throws SQLException {
        String publicIdLogo = null;
        String publicIdRestaurante = null;

        String nombre = ctx.formParam("nombre");
        String telefono = ctx.formParam("telefono");
        String direccion = ctx.formParam("direccion");
        String horario_apertura = ctx.formParam("horario_apertura");
        String horario_cierre = ctx.formParam("horario_cierre");
        String id_usuario = ctx.formParam("id_usuario");

        System.out.println(id_usuario);

        // Validate required fields
        if (nombre == null || telefono == null || direccion == null ||
                horario_apertura == null || horario_cierre == null || id_usuario == null) {
            ctx.status(400).json(Map.of("error", "Todos los campos son requeridos"));
            return;
        }

        Time TimeHorario_apertura;
        Time TimeHorario_cierre;
        int IntegerId_usario;

        try {
            TimeHorario_apertura = Time.valueOf(horario_apertura);
            TimeHorario_cierre = Time.valueOf(horario_cierre);
            IntegerId_usario = Integer.parseInt(id_usuario);
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "Formato de datos inválido"));
            return;
        }

        System.out.println(TimeHorario_apertura);
        System.out.println(TimeHorario_cierre);

        String logo_url = null;
        String banner_url = null;

        UploadedFile logo = ctx.uploadedFile("logo");
        UploadedFile banner = ctx.uploadedFile("banner");

        // Check if logo is provided
        if (logo == null) {
            ctx.status(400).json(Map.of("error", "El logo es requerido"));
            return;
        }

        // Validate logo file type
        String contentTypeLogo = logo.contentType();
        if (contentTypeLogo == null || !contentTypeLogo.startsWith("image/")) {
            ctx.status(400).json(Map.of("error", "El logo debe ser una imagen"));
            return;
        }

        // Check if banner is provided
        if (banner == null) {
            ctx.status(400).json(Map.of("error", "El banner es requerido"));
            return;
        }

        // Validate banner file type
        String contentTypeBanner = banner.contentType();
        if (contentTypeBanner == null || !contentTypeBanner.startsWith("image/")) {
            ctx.status(400).json(Map.of("error", "El banner debe ser una imagen"));
            return;
        }

        // Generate unique IDs for images
        publicIdLogo = "logo_" + UUID.randomUUID().toString();
        publicIdRestaurante = "restaurante_" + UUID.randomUUID().toString();

        // Upload to Cloudinary
        try (InputStream logoStream = logo.content();
             InputStream bannerStream = banner.content()) {

            byte[] logoBytes = logoStream.readAllBytes();
            logo_url = cloudinaryService.uploadImageRestaurant(logoBytes, publicIdLogo);

            byte[] bannerBytes = bannerStream.readAllBytes();
            banner_url = cloudinaryService.uploadImageRestaurant(bannerBytes, publicIdRestaurante);

        } catch (IOException e) {
            ctx.status(500).json(Map.of("error", "Error al procesar la imagen: " + e.getMessage()));
            return;
        }

        try {
            Restaurante restaurante = new Restaurante();
            restaurante.setLogo_url(logo_url);
            restaurante.setBanner_url(banner_url);
            restaurante.setNombre_restaurante(nombre);
            restaurante.setTelefono(telefono);
            restaurante.setDireccion(direccion);
            restaurante.setHorario_apertura(TimeHorario_apertura);
            restaurante.setHorario_cierre(TimeHorario_cierre);
            restaurante.setId_usuario(IntegerId_usario);

            Restaurante saveRestaurante = restauranteRepository.save(restaurante);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Restaurante agregado exitosamente");
            response.put("product", Map.of(
                    "id_restaurante", saveRestaurante.getId_restaurante(),
                    "logo_url", saveRestaurante.getLogo_url(),
                    "banner_url", saveRestaurante.getBanner_url(),
                    "nombre", saveRestaurante.getNombre_restaurante(),
                    "telefono", saveRestaurante.getTelefono(),
                    "direccion", saveRestaurante.getDireccion(),
                    "horario_apertura", saveRestaurante.getHorario_apertura(),
                    "horario_cierre", saveRestaurante.getHorario_cierre(),
                    "id_usario", saveRestaurante.getId_usuario()
            ));
            ctx.status(200).json(response);

        } catch (Exception e) {
            e.printStackTrace();

            // If there's an error and images were uploaded, delete them from Cloudinary
            if (publicIdLogo != null || publicIdRestaurante != null) {
                try {
                    cloudinaryService.deleteImage(publicIdLogo);
                    cloudinaryService.deleteImage(publicIdRestaurante);
                } catch (Exception deleteError) {
                    System.err.println("Error al eliminar imagen de Cloudinary: " + deleteError.getMessage());
                }
            }

            ctx.status(500).json(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }


    public void updateRestaurante(Context ctx) throws SQLException {
        String publicIdLogo = null;
        String publicIdRestaurante = null;

        int id_restaurante = Integer.parseInt(ctx.pathParam("id"));
        String nombre = ctx.formParam("nombre");
        String telefono = ctx.formParam("telefono");
        String direccion = ctx.formParam("direccion");
        String horario_apertura = ctx.formParam("horario_apertura");
        String horario_cierre = ctx.formParam("horario_cierre");
        String id_usuario = ctx.formParam("id_usuario");
        String nombre_usuario = ctx.formParam("nombre_usuario");
        String telefono_usuario = ctx.formParam("telefono_usuario");

        Time TimeHorario_apertura = Time.valueOf(horario_apertura);
        Time TimeHorario_cierre = Time.valueOf(horario_cierre);
        int IntegerId_usario = Integer.parseInt(id_usuario);

        // Obtener URLs actuales antes de procesar
        Restaurante restauranteActual = restauranteRepository.getRestauranteById(id_restaurante);
        String logo_url = restauranteActual != null ? restauranteActual.getLogo_url() : null;
        String banner_url = restauranteActual != null ? restauranteActual.getBanner_url() : null;

        UploadedFile logo = ctx.uploadedFile("logo");
        UploadedFile banner = ctx.uploadedFile("banner");

        try {
            // Procesar logo solo si se proporciona uno nuevo
            if (logo != null) {
                String contentTypeLogo = logo.contentType();
                if (contentTypeLogo == null || !contentTypeLogo.startsWith("image/")) {
                    ctx.status(400).json(Map.of("error", "El archivo de logo debe ser una imagen"));
                    return;
                }

                publicIdLogo = "logo_" + UUID.randomUUID().toString();
                try (InputStream logoStream = logo.content()) {
                    byte[] logoBytes = logoStream.readAllBytes();
                    logo_url = cloudinaryService.uploadImageRestaurant(logoBytes, publicIdLogo);
                } catch (IOException e) {
                    ctx.status(500).json(Map.of("error", "Error al procesar el logo: " + e.getMessage()));
                    return;
                }
            }

            // Procesar banner solo si se proporciona uno nuevo
            if (banner != null) {
                String contentTypeBanner = banner.contentType();
                if (contentTypeBanner == null || !contentTypeBanner.startsWith("image/")) {
                    ctx.status(500).json(Map.of("error", "El archivo de banner debe ser una imagen"));
                    return;
                }

                publicIdRestaurante = "restaurante_" + UUID.randomUUID().toString();
                try (InputStream bannerStream = banner.content()) {
                    byte[] bannerBytes = bannerStream.readAllBytes();
                    banner_url = cloudinaryService.uploadImageRestaurant(bannerBytes, publicIdRestaurante);
                } catch (IOException e) {
                    ctx.status(500).json(Map.of("error", "Error al procesar el banner: " + e.getMessage()));
                    return;
                }
            }

            // Crear objeto restaurante con URLs actuales o nuevas
            Restaurante restaurante = new Restaurante();
            restaurante.setLogo_url(logo_url); // Será la existente si no hay nueva
            restaurante.setBanner_url(banner_url); // Será la existente si no hay nueva
            restaurante.setNombre_restaurante(nombre);
            restaurante.setTelefono(telefono);
            restaurante.setDireccion(direccion);
            restaurante.setHorario_apertura(TimeHorario_apertura);
            restaurante.setHorario_cierre(TimeHorario_cierre);
            restaurante.setId_usuario(IntegerId_usario);
            restaurante.setNombre_usuario(nombre_usuario);
            restaurante.setTelefono_usuario(telefono_usuario);

            // Actualizar en base de datos
            Restaurante saveRestaurante = restauranteRepository.updateRestaurante(id_restaurante, restaurante);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Restaurante actualizado exitosamente");
            response.put("Restaurante", Map.of(
                    "id_restaurante", saveRestaurante.getId_restaurante(),
                    "logo_url", saveRestaurante.getLogo_url(),
                    "banner_url", saveRestaurante.getBanner_url(),
                    "nombre", saveRestaurante.getNombre_restaurante(),
                    "telefono", saveRestaurante.getTelefono(),
                    "direccion", saveRestaurante.getDireccion(),
                    "horario_apertura", saveRestaurante.getHorario_apertura(),
                    "horario_cierre", saveRestaurante.getHorario_cierre(),
                    "id_usuario", saveRestaurante.getId_usuario()
            ));
            ctx.status(HttpStatus.OK).json(response);

        } catch (Exception e) {
            // Si hay error y se subieron imágenes nuevas, eliminarlas de Cloudinary
            if (publicIdLogo != null) {
                try {
                    cloudinaryService.deleteImage(publicIdLogo);
                } catch (Exception deleteError) {
                    System.err.println("Error al eliminar logo de Cloudinary: " + deleteError.getMessage());
                }
            }
            if (publicIdRestaurante != null) {
                try {
                    cloudinaryService.deleteImage(publicIdRestaurante);
                } catch (Exception deleteError) {
                    System.err.println("Error al eliminar banner de Cloudinary: " + deleteError.getMessage());
                }
            }

            ctx.status(500).json(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    public void createTransport(Context ctx) throws  SQLException {
        String publicId = null;

        try {
            // Validar que la request sea multipart
            if (!ctx.isMultipart()) {
                ctx.status(400).json(Map.of("error", "Request debe ser multipart/form-data"));
                return;
            }

            // Obtener parámetros del formulario
            String placas = ctx.formParam("placas");
            String id_vehiculo = ctx.formParam("id_vehiculo");
            String id_usuario = ctx.formParam("id_usuario");

            // Parsear valores numéricos
            int id_vehiculoInteger;
            int id_usuarioInteger;

            try {
                id_vehiculoInteger = Integer.parseInt(id_vehiculo);
                id_usuarioInteger = Integer.parseInt(id_usuario);

            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of("error", "Formato numérico inválido"));
                return;
            }

            // Procesar imagen (opcional)
            String urlImagen = null;
            UploadedFile file = ctx.uploadedFile("imagen");

            if (file != null) {
                // Validar tipo de archivo
                String contentType = file.contentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    ctx.status(400).json(Map.of("error", "El archivo debe ser una imagen"));
                    return;
                }

                // Validar tamaño de archivo (ejemplo: máximo 5MB)
                if (file.size() > 5 * 1024 * 1024) {
                    ctx.status(400).json(Map.of("error", "El archivo es demasiado grande (máximo 5MB)"));
                    return;
                }

                // Generar ID único para la imagen
                publicId = "product_" + UUID.randomUUID().toString();

                // Subir a Cloudinary
                try (InputStream imageStream = file.content()){
                    byte[] imageBytes = imageStream.readAllBytes();
                    urlImagen = cloudinaryService.uploadImage(imageBytes, publicId);
                } catch (IOException e) {
                    ctx.status(500).json(Map.of("error", "Error al procesar la imagen: " + e.getMessage()));
                    return;
                }
                // Crear objeto producto
               Transport transport = new Transport();
                transport.setId_vehiculo(id_vehiculoInteger);
                transport.setId_usuario(id_usuarioInteger);
                transport.setPlacas(placas);
                transport.setUrl_foto_vehicle(urlImagen);

                // Guardar en base de datos
                int savedTransport = transportRepository.saveTransport(transport);

                // Respuesta exitosa
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "vehiculo actualizado exitosamente");
                response.put("vehiculo", Map.of(
                        "id", savedTransport,
                        "id_vehiculo", transport.getId_vehiculo(),
                        "id_usuario", transport.getId_usuario(),
                        "placas", transport.getPlacas(),
                        "url_Imagen", transport.getUrl_foto_vehicle() != null ? transport.getUrl_foto_vehicle() : ""));

                ctx.status(200).json(response);
            }
        }catch (Exception e) {
            e.printStackTrace();

            // Si hay error y se subió imagen, eliminarla de Cloudinary
            if (publicId != null) {
                try {
                    cloudinaryService.deleteImage(publicId);
                } catch (Exception deleteError) {
                    System.err.println("Error al eliminar imagen de Cloudinary: " + deleteError.getMessage());
                }
            }

            ctx.status(500).json(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }

    }

    public void updateTransport(Context ctx) throws  SQLException {
        String publicId = null;
        try {
            // Validar que la request sea multipart
            if (!ctx.isMultipart()) {
                ctx.status(400).json(Map.of("error", "Request debe ser multipart/form-data"));
                return;
            }

            // Obtener parámetros del formulario
            String placas = ctx.formParam("placas");
            String id_vehiculo = ctx.formParam("id_vehiculo");
            String id_usuario = ctx.formParam("id_usuario");


            // Parsear valores numéricos
            int id_vehiculoInteger;
            int id_usuarioInteger;

            try {
                id_vehiculoInteger = Integer.parseInt(id_vehiculo);
                id_usuarioInteger = Integer.parseInt(id_usuario);
            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of("error", "Formato numérico inválido"));
                e.printStackTrace();
                return;
            }

            // Procesar imagen (opcional)
            String urlImagen = null;
            UploadedFile file = ctx.uploadedFile("imagen");

            if (file != null) {
                // Validar tipo de archivo
                String contentType = file.contentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    ctx.status(400).json(Map.of("error", "El archivo debe ser una imagen"));
                    return;
                }

                // Validar tamaño de archivo (ejemplo: máximo 5MB)
                if (file.size() > 5 * 1024 * 1024) {
                    ctx.status(400).json(Map.of("error", "El archivo es demasiado grande (máximo 5MB)"));
                    return;
                }

                // Generar ID único para la imagen
                publicId = "product_" + UUID.randomUUID().toString();

                // Subir a Cloudinary
                try (InputStream imageStream = file.content()){
                    byte[] imageBytes = imageStream.readAllBytes();
                    urlImagen = cloudinaryService.uploadImage(imageBytes, publicId);
                } catch (IOException e) {
                    ctx.status(500).json(Map.of("error", "Error al procesar la imagen: " + e.getMessage()));
                    return;
                }
                // Crear objeto producto
                Transport transport = new Transport();
                transport.setId_vehiculo(id_vehiculoInteger);
                transport.setId_usuario(id_usuarioInteger);
                transport.setPlacas(placas);
                transport.setUrl_foto_vehicle(urlImagen);

                // Guardar en base de datos
                int savedTransport = transportRepository.updateTransport(id_usuarioInteger, transport);

                // Respuesta exitosa
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Producto creado exitosamente");
                response.put("product", Map.of(
                        "id", savedTransport,
                        "id_vehiculo", transport.getId_vehiculo(),
                        "id_usuario", transport.getId_usuario(),
                        "placas", transport.getPlacas(),
                        "url_Imagen", transport.getUrl_foto_vehicle() != null ? transport.getUrl_foto_vehicle() : ""));

                ctx.status(200).json(response);
            }
        } catch (Exception e) {
            e.printStackTrace();

            // Si hay error y se subió imagen, eliminarla de Cloudinary
            if (publicId != null) {
                try {
                    cloudinaryService.deleteImage(publicId);
                } catch (Exception deleteError) {
                    System.err.println("Error al eliminar imagen de Cloudinary: " + deleteError.getMessage());
                }
            }

            ctx.status(500).json(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
}