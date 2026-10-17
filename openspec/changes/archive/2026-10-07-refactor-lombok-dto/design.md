# Design

## Context

Actualmente las clases del dominio heredan de la clase abstracta `Base` e implementan getters, setters, constructores con y sin argumentos manualmente, lo cual incrementa el volumen de código y la posibilidad de errores. Adicionalmente, el proyecto carece de una herramienta formal de gestión de dependencias, por lo que se migrará a Gradle, y las entidades exponen atributos sensibles como contraseñas, lo que será resuelto mediante DTOs.

## Goals / Non-Goals

**Goals:**
- Configurar Gradle como herramienta de automatización y gestión de dependencias (Lombok, Jakarta, Hibernate, MySQL).
- Refactorizar las entidades del modelo (`Base`, `Usuario`, `Pedido`, `Categoria`, `Producto`, `DetallePedido`) utilizando anotaciones de Lombok.
- Implementar el patrón DTO para la clase `Usuario` mediante Java Records para ocultar atributos sensibles (como el password) en respuestas.
- Preservar los métodos de negocio preexistentes (como `calcularTotal()`).

**Non-Goals:**
- Alterar la lógica de negocio subyacente.
- Cambiar la base de datos o su esquema (solo se moderniza el código Java).
- Crear DTOs para el resto de las entidades, excepto que sean estrictamente necesarios para ocultar datos (se prioriza `UsuarioDTO`).

## Decisions

- **Uso de `@SuperBuilder` para la Herencia:** 
  - *Rationale*: Puesto que tenemos una clase `Base` y múltiples subclases, el `@Builder` estándar de Lombok no funciona bien con la herencia (los constructores generados no inicializan los campos del padre fácilmente). `@SuperBuilder` resuelve esto, permitiendo construir una subclase inicializando también los atributos heredados de `Base`.
  - *Alternativas*: Usar constructores normales (contradice el propósito de Lombok) o usar `@Builder` en cada subclase redefiniendo propiedades (viola DRY).
- **Uso de `@Data`, `@NoArgsConstructor` y `@AllArgsConstructor`:** 
  - *Rationale*: `@Data` provee getters, setters, equals, hashCode y toString automáticamente. `@NoArgsConstructor` es requerido por JPA para instanciar las entidades a través de reflexión, y `@AllArgsConstructor` es útil para tests y constructores completos.
- **Implementación de `UsuarioDTO` como Java Record:** 
  - *Rationale*: Los Records (introducidos en Java 14) son clases inmutables ideales para transportar datos (DTOs). Son más limpios, no requieren getters tradicionales ni Lombok, y garantizan que los datos expuestos no se modifiquen inadvertidamente. Solo incluirán campos públicos seguros (ej. id, nombre, email) y omitirán la contraseña.
  - *Alternativas*: Clases normales con Lombok `@Data` (menos seguras por mutabilidad, aunque viables si se usa `@Value`).

## Risks / Trade-offs

- **[Risk] Configuración de Lombok en el IDE (VS Code):** Si el IDE no tiene habilitado el Annotation Processor, puede mostrar errores de compilación falsos.
  - *Mitigation*: Asegurar que el plugin de Lombok y las configuraciones de `build.gradle` (como `annotationProcessor`) estén correctamente definidos.
- **[Risk] Sobrescritura de métodos de negocio:** Podrían borrarse por accidente métodos como `calcularTotal()` al limpiar getters/setters manuales.
  - *Mitigation*: Proceder con cuidado, eliminando únicamente constructores y accesadores estándar.
