# Proposal

## Why

El código actual contiene mucho código repetitivo (boilerplate) como getters, setters y constructores, lo cual dificulta la lectura y el mantenimiento. Además, las entidades de dominio exponen directamente sus campos, lo que puede ser inseguro para datos sensibles. Esta refactorización mejora la limpieza del código usando Lombok y la seguridad en la capa de presentación usando Java Records para DTOs.

## What Changes

- Refactorización de las entidades del modelo de dominio (`Base`, `Usuario`, `Pedido`, `Categoria`, `Producto`, `DetallePedido`) para eliminar getters, setters y constructores repetitivos mediante el uso de anotaciones de Lombok (`@Data`, `@SuperBuilder`, etc.).
- Creación de un `UsuarioDTO` implementado como un Java Record para ocultar información sensible (como contraseñas) al momento de exponer los datos.
- Migración y configuración del proyecto para utilizar Gradle como sistema de construcción, asegurando que las dependencias (Lombok, JPA/Hibernate, MySQL) estén correctamente gestionadas.

## Capabilities

### New Capabilities
<!-- No hay nuevas funcionalidades a nivel de especificación (es refactor) -->

### Modified Capabilities
<!-- No hay cambios en funcionalidades existentes a nivel de especificación -->

## Impact

- **Código Afectado**: Entidades del modelo de dominio. Capa DTO y configuración del proyecto.
- **Dependencias**: Se añadirá/actualizará Gradle, Lombok y se mantendrán Jakarta Persistence, Hibernate y el conector MySQL.
- **Sistemas**: El comportamiento observable de la aplicación debe mantenerse exactamente igual (pure refactor).
