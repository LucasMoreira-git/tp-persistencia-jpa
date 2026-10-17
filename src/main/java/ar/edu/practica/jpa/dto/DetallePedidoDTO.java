package ar.edu.practica.jpa.dto;

public record DetallePedidoDTO(
    Long id,
    int cantidad,
    Double subtotal,
    String productoNombre
) {
}
