package com.moises.almacen.mappers;

import com.moises.almacen.dto.sucursales.SucursalesResponse;
import com.moises.almacen.dto.ventas.DetalleVentaResponse;
import com.moises.almacen.dto.ventas.ReporteVentasProjection;
import com.moises.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.moises.almacen.dto.ventas.VentaResponse;
import com.moises.almacen.entities.DetalleVenta;
import com.moises.almacen.entities.Venta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VentaMapper {

    private final SucursalMapper sucursalMapper;

    public VentaMapper(SucursalMapper sucursalMapper) {
        this.sucursalMapper = sucursalMapper;
    }

    public VentaResponse toResponse(Venta venta) {
        if (venta == null) return null;

        // Usar el método entidadAResponse de SucursalMapper
        SucursalesResponse sucursalResponse = sucursalMapper.entidadAResponse(venta.getSucursal());

        // Mapear los detalles
        List<DetalleVentaResponse> detallesResponse = venta.getDetalleVentas().stream()
                .map(this::detalleToResponse)
                .collect(Collectors.toList());

        // Calcular el total
        BigDecimal total = detallesResponse.stream()
                .map(DetalleVentaResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new VentaResponse(
                venta.getId(),
                venta.getFecha().toString(),
                venta.getEstadoVenta().name(),
                sucursalResponse,
                detallesResponse,
                total
        );
    }

    private DetalleVentaResponse detalleToResponse(DetalleVenta detalle) {
        BigDecimal subtotal = detalle.getPrecioProducto()
                .multiply(BigDecimal.valueOf(detalle.getCantidadProducto()));

        return new DetalleVentaResponse(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidadProducto(),
                detalle.getPrecioProducto(),
                subtotal
        );
    }

    public List<ReporteVentasSucursalResponse> resultadosAReporte(
            List<ReporteVentasProjection> resultados
    ){
        if (resultados == null) return null;

        return resultados.stream()
                .map( res -> new ReporteVentasSucursalResponse(
                        res.getIdSucursal(),
                        res.getNombreSucursal(),
                        res.getTotalFacturado(),
                        res.getProductosVendidos()
                )).toList();
    }

}