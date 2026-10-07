package ar.edu.practica.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pedidos")
public class Pedido extends Base implements Calculable {

    @Builder.Default
    private LocalDate fecha = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Estado estado = Estado.PENDIENTE;

    @Builder.Default
    private Double total = 0.0;

    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false)
    private Usuario usuario;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido(LocalDate fecha, Estado estado, FormaPago formaPago) {
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
    }

    public void addDetallePedido(int cantidad, Producto producto) {
        DetallePedido detalle = new DetallePedido(cantidad, producto);
        detalle.setPedido(this);
        detalles.add(detalle);
        calcularTotal();
    }

    public DetallePedido findeDetallePedidoByProducto(Producto producto) {
        return detalles.stream()
                .filter(detalle -> Objects.equals(detalle.getProducto().getId(), producto.getId()))
                .findFirst()
                .orElse(null);
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findeDetallePedidoByProducto(producto);
        if (detalle != null) {
            detalles.remove(detalle);
            detalle.setPedido(null);
            calcularTotal();
        }
    }

    @Override
    public void calcularTotal() {
        total = detalles.stream()
                .mapToDouble(detalle -> Optional.ofNullable(detalle.getSubtotal()).orElse(0.0))
                .sum();
    }
}