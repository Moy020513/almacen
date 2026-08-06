package com.moises.almacen.services.Ventas;

import com.moises.almacen.dto.ventas.DetalleVentaRequest;
import com.moises.almacen.dto.ventas.VentaRequest;
import com.moises.almacen.dto.ventas.VentaResponse;
import com.moises.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.moises.almacen.entities.DetalleVenta;
import com.moises.almacen.entities.Producto;
import com.moises.almacen.entities.Sucursal;
import com.moises.almacen.entities.Venta;
import com.moises.almacen.enums.EstadoVenta;
import com.moises.almacen.exceptions.RecursoNoEncontradoException;
import com.moises.almacen.mappers.VentaMapper;
import com.moises.almacen.repositories.ProductoRepository;
import com.moises.almacen.repositories.SucursalRepository;
import com.moises.almacen.repositories.VentaRepository;
import com.moises.almacen.services.Ventas.VentaService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final VentaMapper ventaMapper;

    @Override
    @Transactional
    public VentaResponse registrar(VentaRequest request) {
        log.info("Registrando nueva venta para sucursal ID: {}", request.idSucursal());

        Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con ID: " + request.idSucursal()));

        Venta venta = Venta.builder()
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .sucursal(sucursal)
                .detalleVentas(new ArrayList<>())
                .build();

        for (DetalleVentaRequest detalleRequest : request.productos()) {
            procesarDetalleVenta(venta, detalleRequest);
        }

        Venta ventaGuardada = ventaRepository.save(venta);
        log.info("Venta registrada exitosamente con ID: {}", ventaGuardada.getId());

        return ventaMapper.toResponse(ventaGuardada);
    }

    private void procesarDetalleVenta(Venta venta, DetalleVentaRequest detalleRequest) {
        Producto producto = productoRepository.findById(detalleRequest.idProducto())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con ID: " + detalleRequest.idProducto()));

        if (producto.getCantidad() < detalleRequest.cantidadProducto()) {
            throw new IllegalArgumentException(
                    String.format("Stock insuficiente para el producto '%s' (ID: %d) ",
                            producto.getNombre(),
                            producto.getId(),
                            producto.getCantidad(),
                            detalleRequest.cantidadProducto())
            );
        }

        producto.descontarCantidad(detalleRequest.cantidadProducto());

        DetalleVenta detalle = DetalleVenta.builder()
                .venta(venta)
                .producto(producto)
                .cantidadProducto(detalleRequest.cantidadProducto())
                .precioProducto(producto.getPrecio()) // Precio del momento
                .build();

        venta.getDetalleVentas().add(detalle);
    }

    @Override
    @Transactional
    public VentaResponse cancelar(Long idVenta) {
        log.info("Cancelando venta con ID: {}", idVenta);

        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Venta no encontrada con ID: " + idVenta));

        if (venta.getEstadoVenta() == EstadoVenta.CANCELADA) {
            throw new IllegalArgumentException(
                    "La venta con ID " + idVenta + " ya se encuentra cancelada");
        }

        // Devolver stock
        for (DetalleVenta detalle : venta.getDetalleVentas()) {
            Producto producto = detalle.getProducto();
            producto.aumentarCantidad(detalle.getCantidadProducto());
        }

        venta.cancelar();
        Venta ventaCancelada = ventaRepository.save(venta);

        log.info("Venta {} cancelada exitosamente", idVenta);
        return ventaMapper.toResponse(ventaCancelada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarActivas() {
        log.info("Listando ventas activas (estado: REGISTRADA)");
        return ventaRepository.findByEstadoVenta(EstadoVenta.REGISTRADA)
                .stream()
                .map(ventaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {
        log.info("Listando ventas canceladas (estado: CANCELADA)");
        return ventaRepository.findByEstadoVenta(EstadoVenta.CANCELADA)
                .stream()
                .map(ventaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> generarReportePorSucursal() {
        log.info("Generando reporte de ventas por sucursal");


        return ventaMapper.resultadosAReporte(ventaRepository.getReporteVentasPorSucursal(EstadoVenta.REGISTRADA));
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorId(Long id) {
        log.info("Obteniendo venta con ID: {}", id);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Venta no encontrada con ID: " + id));
        return ventaMapper.toResponse(venta);
    }
}