package com.moises.almacen.dto.ventas;

import com.moises.almacen.dto.sucursales.SucursalesResponse;
import com.moises.almacen.entities.Sucursal;

import java.math.BigDecimal;
import java.util.List;

public record VentaResponse(
        Long id,
        String fecha,
        String estado,
        SucursalesResponse sucursal,
        List<DetalleVentaResponse> detalles,
        BigDecimal total
) {
}
