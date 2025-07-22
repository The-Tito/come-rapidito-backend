package org.alilopez.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.Map;
import java.io.IOException;
import io.github.cdimascio.dotenv.Dotenv;

public class CloudinaryService {
    private Cloudinary cloudinary;

    public CloudinaryService() {
        Dotenv dotenv = Dotenv.load();
        try {
            // Opción 1: Usar variables de entorno
            String cloudinaryUrl = dotenv.get("CLOUDINARY_URL");

            if (cloudinaryUrl != null && !cloudinaryUrl.isEmpty()) {
                // Configurar con URL de entorno
                this.cloudinary = new Cloudinary(cloudinaryUrl);
            } else {
                // Opción 2: Configurar manualmente
                this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", dotenv.get("CLOUDINARY_NAME"),
                        "api_key", dotenv.get("CLOUDINARY_API_KEY"),
                        "api_secret", dotenv.get("CLOUDINARY_API_SECRET")
                ));
            }

            // Verificar configuración
            if (this.cloudinary.config.cloudName == null || this.cloudinary.config.cloudName.isEmpty()) {
                throw new RuntimeException("Cloudinary no está configurado correctamente. Verifica las variables de entorno.");
            }

            System.out.println("Cloudinary configurado para: " + this.cloudinary.config.cloudName);

        } catch (Exception e) {
            System.err.println("Error al configurar Cloudinary: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar Cloudinary", e);
        }
    }

    public String uploadImage(byte[] imageBytes, String publicId) throws IOException {
        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "public_id", publicId,
                    "resource_type", "image",
                    "folder", "products" // Organizar en carpetas
            );

            Map<String, Object> result = cloudinary.uploader().upload(imageBytes, uploadParams);
            return (String) result.get("secure_url");

        } catch (Exception e) {
            System.err.println("Error al subir imagen: " + e.getMessage());
            throw new IOException("Error al subir imagen a Cloudinary", e);
        }
    }

    public String uploadImageRestaurant(byte[] imageBytes, String publicId) throws IOException {
        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "public_id", publicId,
                    "resource_type", "image",
                    "folder", "restaurant" // Organizar en carpetas
            );

            Map<String, Object> result = cloudinary.uploader().upload(imageBytes, uploadParams);
            return (String) result.get("secure_url");

        } catch (Exception e) {
            System.err.println("Error al subir imagen: " + e.getMessage());
            throw new IOException("Error al subir imagen a Cloudinary", e);
        }
    }

    public void deleteImage(String publicId) throws IOException {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy("products/" + publicId, ObjectUtils.emptyMap());
            System.out.println("Imagen eliminada: " + result.get("result"));
        } catch (Exception e) {
            System.err.println("Error al eliminar imagen: " + e.getMessage());
            throw new IOException("Error al eliminar imagen de Cloudinary", e);
        }
    }


}