package ar.edu.practica.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PedidoPersistenceTest {
    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    static void abrirUnidadDePersistencia() {
        entityManagerFactory = Persistence.createEntityManagerFactory("miUnidad");
    }

    @AfterAll
    static void cerrarUnidadDePersistencia() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @Test
    void persisteProductoYCalculaTotalDelPedido() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Categoria categoria = Categoria.builder()
                .nombre("Bebidas")
                .descripcion("Bebidas frías")
                .build();

        Producto producto = Producto.builder()
                .nombre("Agua")
                .precio(500.0)
                .descripcion("Agua mineral")
                .stock(10)
                .imagen("agua.jpg")
                .disponible(true)
                .categoria(categoria)
                .build();

        Pedido pedido = Pedido.builder().build();

        Usuario usuario = Usuario.builder()
                .nombre("Ana")
                .apellido("García")
                .mail("ana@mail.com")
                .celular("111111111")
                .contrasena("secreto")
                .rol(Rol.ADMIN)
                .build();

        usuario.agregarPedido(pedido);
        pedido.addDetallePedido(2, producto);

        entityManager.getTransaction().begin();
        entityManager.persist(categoria);
        entityManager.persist(producto);
        entityManager.persist(usuario);
        entityManager.getTransaction().commit();

        assertEquals(1000.0, pedido.getTotal());
        assertEquals(1L, entityManager.createQuery("select count(p) from Producto p", Long.class)
                .getSingleResult());

        entityManager.close();
    }
}