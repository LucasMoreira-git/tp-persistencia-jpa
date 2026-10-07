package ar.edu.practica.jpa.dto;

public record ProductoDTO(
    Long id,
    String nombre,
    Double precio,
    String descripcion,
    int stock,
    String imagen,
    boolean disponible,
    String categoriaNombre
) {
}
