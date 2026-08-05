package com.moises.almacen.dto.productos;

import java.math.BigDecimal;

public record ProductosResponse(
        Long id,
        String nombre,
        String categoria,
        BigDecimal precio,
        Integer cantidad
) {
}
