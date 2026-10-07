package ar.edu.practica.jpa;

import java.time.LocalDate;
import java.util.List;

import ar.edu.practica.jpa.dto.DtoMapper;
import ar.edu.practica.jpa.dto.UsuarioDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class PracticaService {
    public void ejecutarTrabajoPractico() {
        EntityManager entityManager = JpaUtil.entityManagerFactory().createEntityManager();
        try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            List<Categoria> categorias = crearCategorias();
            categorias.forEach(entityManager::persist);

            List<Producto> productos = crearProductos(categorias);
            productos.forEach(entityManager::persist);

            Usuario usuarioAdmin = Usuario.builder()
                    .nombre("Ana")
                    .apellido("García")
                    .mail("ana@mail.com")
                    .celular("111111111")
                    .contrasena("secreto")
                    .rol(Rol.ADMIN)
                    .build();

            Usuario usuario = Usuario.builder()
                    .nombre("Luis")
                    .apellido("Pérez")
                    .mail("luis@mail.com")
                    .celular("222222222")
                    .contrasena("secreto")
                    .rol(Rol.USUARIO)
                    .build();

            entityManager.persist(usuarioAdmin);
            entityManager.persist(usuario);

            crearPedidos(usuarioAdmin, usuario, productos).forEach(entityManager::persist);
            transaction.commit();

            actualizarProductos(entityManager, productos.get(0), productos.get(1));
            buscarUsuarios(entityManager, usuarioAdmin.getId(), "luis@mail.com");
            eliminarProducto(entityManager, productos.get(9));
        } finally {
            entityManager.close();
        }
    }

    private List<Categoria> crearCategorias() {
        return List.of(
                Categoria.builder().nombre("Bebidas").descripcion("Bebidas frías y calientes").build(),
                Categoria.builder().nombre("Almacén").descripcion("Productos de almacén").build(),
                Categoria.builder().nombre("Limpieza").descripcion("Artículos de limpieza").build()
        );
    }

    private List<Producto> crearProductos(List<Categoria> categorias) {
        return List.of(
                producto("Tatin", 500.0, categorias.get(0)),
                producto("Coca-Cola", 1200.0, categorias.get(0)),
                producto("Jugo de naranja", 900.0, categorias.get(0)),
                producto("Arroz", 1500.0, categorias.get(1)),
                producto("Fideos", 800.0, categorias.get(1)),
                producto("Yerba", 2500.0, categorias.get(1)),
                producto("Jabón", 700.0, categorias.get(2)),
                producto("Lavandina", 1100.0, categorias.get(2)),
                producto("Esponja", 400.0, categorias.get(2)),
                producto("Detergente", 1300.0, categorias.get(2))
        );
    }

    private Producto producto(String nombre, double precio, Categoria categoria) {
        return Producto.builder()
                .nombre(nombre)
                .precio(precio)
                .descripcion("Producto " + nombre)
                .stock(20)
                .imagen(nombre.toLowerCase() + ".jpg")
                .disponible(true)
                .categoria(categoria)
                .build();
    }

    private List<Pedido> crearPedidos(Usuario admin, Usuario usuario, List<Producto> productos) {
        Pedido primero = pedido(admin, FormaPago.TARJETA);
        primero.addDetallePedido(2, productos.get(0));
        primero.addDetallePedido(1, productos.get(1));

        Pedido segundo = pedido(usuario, FormaPago.TRANSFERENCIA);
        segundo.addDetallePedido(3, productos.get(2));
        segundo.addDetallePedido(2, productos.get(3));

        Pedido tercero = pedido(usuario, FormaPago.EFECTIVO);
        tercero.addDetallePedido(1, productos.get(4));
        tercero.addDetallePedido(4, productos.get(5));
        return List.of(primero, segundo, tercero);
    }

    private Pedido pedido(Usuario usuario, FormaPago formaPago) {
        Pedido pedido = Pedido.builder()
                .fecha(LocalDate.now())
                .estado(Estado.PENDIENTE)
                .formaPago(formaPago)
                .build();
        usuario.agregarPedido(pedido);
        return pedido;
    }

    private void actualizarProductos(EntityManager entityManager, Producto primero, Producto segundo) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        primero.setPrecio(550.0);
        segundo.setStock(35);
        entityManager.merge(primero);
        entityManager.merge(segundo);
        transaction.commit();
    }

    public List<UsuarioDTO> buscarUsuarios(EntityManager entityManager, Long id, String mail) {
        Usuario porId = entityManager.find(Usuario.class, id);
        Usuario porMail = entityManager.createQuery(
                        "select u from Usuario u where u.mail = :mail", Usuario.class)
                .setParameter("mail", mail)
                .getSingleResult();

        UsuarioDTO dtoPorId = DtoMapper.toDTO(porId);
        UsuarioDTO dtoPorMail = DtoMapper.toDTO(porMail);

        System.out.printf("Usuario por id (DTO seguro): %s %s (Email: %s, Rol: %s)%n",
                dtoPorId.nombre(), dtoPorId.apellido(), dtoPorId.mail(), dtoPorId.rol());
        System.out.printf("Usuario por mail (DTO seguro): %s %s (Email: %s, Rol: %s)%n",
                dtoPorMail.nombre(), dtoPorMail.apellido(), dtoPorMail.mail(), dtoPorMail.rol());

        return List.of(dtoPorId, dtoPorMail);
    }

    private void eliminarProducto(EntityManager entityManager, Producto producto) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Producto productoManaged = entityManager.find(Producto.class, producto.getId());
        productoManaged.setEliminado(true);
        entityManager.remove(productoManaged);
        transaction.commit();
    }
}