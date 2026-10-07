package ar.edu.practica.jpa.dto;

import ar.edu.practica.jpa.DetallePedido;
import ar.edu.practica.jpa.Pedido;
import ar.edu.practica.jpa.Producto;
import ar.edu.practica.jpa.Usuario;

import java.util.Collections;
import java.util.List;

public class DtoMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getMail(),
            usuario.getCelular(),
            usuario.getRol()
        );
    }

    public static ProductoDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        String categoriaNombre = (producto.getCategoria() != null) ? producto.getCategoria().getNombre() : null;
        return new ProductoDTO(
            producto.getId(),
            producto.getNombre(),
            producto.getPrecio(),
            producto.getDescripcion(),
            producto.getStock(),
            producto.getImagen(),
            producto.isDisponible(),
            categoriaNombre
        );
    }

    public static DetallePedidoDTO toDTO(DetallePedido detalle) {
        if (detalle == null) {
            return null;
        }
        String productoNombre = (detalle.getProducto() != null) ? detalle.getProducto().getNombre() : null;
        return new DetallePedidoDTO(
            detalle.getId(),
            detalle.getCantidad(),
            detalle.getSubtotal(),
            productoNombre
        );
    }

    public static PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        Long usuarioId = (pedido.getUsuario() != null) ? pedido.getUsuario().getId() : null;
        List<DetallePedidoDTO> detalles = (pedido.getDetalles() != null)
            ? pedido.getDetalles().stream().map(DtoMapper::toDTO).toList()
            : Collections.emptyList();

        return new PedidoDTO(
            pedido.getId(),
            pedido.getFecha(),
            pedido.getEstado(),
            pedido.getTotal(),
            pedido.getFormaPago(),
            usuarioId,
            detalles
        );
    }
}
