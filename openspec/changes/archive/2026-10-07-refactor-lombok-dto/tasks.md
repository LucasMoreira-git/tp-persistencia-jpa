# Tasks

## 1. Configuración del Proyecto y Dependencias

- [x] 1.1 Revisar y verificar la configuración de dependencias (Lombok, JPA Hibernate, base de datos H2/MySQL) y procesador de anotaciones en Maven (`pom.xml`).
- [x] 1.2 Confirmar compilación del proyecto base con Lombok habilitado.

## 2. Creación de la Capa DTO y Mappers

- [x] 2.1 Crear el Java Record `UsuarioDTO` en el paquete `ar.edu.practica.jpa.dto`, exponiendo atributos públicos (id, nombre, apellido, mail, celular, rol) y omitiendo la contraseña.
- [x] 2.2 Crear los DTOs adicionales (`ProductoDTO`, `PedidoDTO`, `DetallePedidoDTO`) y la clase conversora `DtoMapper`.

## 3. Refactorización de Entidades con Lombok

- [x] 3.1 Modificar la clase abstracta `Base` con las anotaciones `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` y `@SuperBuilder`.
- [x] 3.2 Refactorizar la entidad `Usuario`: agregar anotaciones Lombok (`@Data`, `@EqualsAndHashCode(callSuper = true)`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@SuperBuilder`), excluir relaciones bidireccionales de toString/equals y preservar métodos de negocio.
- [x] 3.3 Refactorizar la entidad `Categoria`: agregar anotaciones Lombok, `@ToString.Exclude` y `@EqualsAndHashCode.Exclude` en productos.
- [x] 3.4 Refactorizar la entidad `Producto`: agregar anotaciones Lombok y excluir `categoria` de toString/equals.
- [x] 3.5 Refactorizar la entidad `Pedido`: agregar anotaciones Lombok, excluir relaciones y mantener intacto `calcularTotal()`, la interfaz `Calculable` y métodos utilitarios.
- [x] 3.6 Refactorizar la entidad `DetallePedido`: agregar anotaciones Lombok y excluir `pedido` y `producto` de toString/equals.

## 4. Actualización de la Capa de Servicio, Tests y Main

- [x] 4.1 Actualizar `PracticaService` para utilizar el patrón Builder (`@SuperBuilder`) y transformar/retornar DTOs mediante `DtoMapper`.
- [x] 4.2 Actualizar `PedidoPersistenceTest` y verificar la suite completa con `mvn test`.
- [x] 4.3 Ejecutar `Main` mediante `mvn exec:java` confirmando persistencia, actualización, consultas DTO y eliminación sin errores.
