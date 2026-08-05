package com.moises.almacen.dto.sucursales;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record SucursalesRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 5, max = 50, message = "El nombre debe tener entre 5 y 30 caracteres")
        String nombre,

        @NotBlank(message = "La dirección es requerida")
        @Size(min = 10, max = 150, message = "El nombre debe tener entre 10 y 150 caracteres")
        String direccion
) {

}
