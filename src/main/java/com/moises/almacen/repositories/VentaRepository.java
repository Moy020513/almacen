package com.moises.almacen.repositories;

import com.moises.almacen.dto.ventas.ReporteVentasProjection;
import com.moises.almacen.entities.Venta;
import com.moises.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByEstadoVenta(EstadoVenta estado);

    // Query JPQL para el reporte de ventas por sucursal
    @Query("""
        SELECT 
        s.id as idSucursal,
        s.nombre as nombreSucursal,
               SUM(d.cantidadProducto * d.precioProducto) AS totalFacturado,
               SUM(d.cantidadProducto) as productosVendidos
        FROM Venta v
        JOIN v.sucursal s
        JOIN v.detalleVentas d
        WHERE v.estadoVenta = :estado
        GROUP BY s.id, s.nombre
    """)
    List<ReporteVentasProjection> getReporteVentasPorSucursal(
            @Param("estado") EstadoVenta estado
    );
}