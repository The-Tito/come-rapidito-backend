package org.alilopez.DTO;

public class ProductDTO {
    private int id_producto;
    private String nombre;
    private float precio;
    private int id_categoria;
    private String nombre_restaurante;
    private int id_restaurante;
    private String descripcion;
    private String url_imagen;
    private int id_status;

    public ProductDTO() {
    }

    public ProductDTO(int id_producto, String nombre, float precio, int id_categoria, String nombre_restaurante, int id_restaurante, String descripcion, String url_imagen, int id_status) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.precio = precio;
        this.id_categoria = id_categoria;
        this.nombre_restaurante = nombre_restaurante;
        this.id_restaurante = id_restaurante;
        this.descripcion = descripcion;
        this.url_imagen = url_imagen;
        this.id_status = id_status;
    }


    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre_restaurante() {
        return nombre_restaurante;
    }

    public void setNombre_restaurante(String nombre_restaurante) {
        this.nombre_restaurante = nombre_restaurante;
    }

    public int getId_restaurante() {
        return id_restaurante;
    }

    public void setId_restaurante(int id_restaurante) {
        this.id_restaurante = id_restaurante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public int getId_status() {
        return id_status;
    }

    public void setId_status(int id_status) {
        this.id_status = id_status;
    }
}
