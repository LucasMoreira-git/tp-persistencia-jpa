package ar.edu.practica.jpa.dto;

import ar.edu.practica.jpa.Estado;
import ar.edu.practica.jpa.FormaPago;

import java.time.LocalDate;
import java.util.List;

public record PedidoDTO(
    Long id,
    LocalDate fecha,
    Estado estado,
    Double total,
    FormaPago formaPago,
    Long usuarioId,
    List<DetallePedidoDTO> detalles
) {
}
