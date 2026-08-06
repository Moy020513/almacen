package com.moises.almacen.dto.ventas;

import java.math.BigDecimal;

public interface ReporteVentasProjection {
    Long getIdSucursal();
    String getNombreSucursal();
    BigDecimal getTotalFacturado();
    Long getProductosVendidos();
}
