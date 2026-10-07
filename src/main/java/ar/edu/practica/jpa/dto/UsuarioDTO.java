package ar.edu.practica.jpa.dto;

import ar.edu.practica.jpa.Rol;

public record UsuarioDTO(
    Long id,
    String nombre,
    String apellido,
    String mail,
    String celular,
    Rol rol
) {
}
