package com.moises.almacen.services.Ventas;

import com.moises.almacen.dto.ventas.VentaRequest;
import com.moises.almacen.dto.ventas.VentaResponse;
import com.moises.almacen.dto.ventas.ReporteVentasSucursalResponse;

import java.util.List;

public interface VentaService {

    VentaResponse registrar(VentaRequest request);

    VentaResponse cancelar(Long idVenta);

    List<VentaResponse> listarActivas();

    List<VentaResponse> listarCanceladas();

    List<ReporteVentasSucursalResponse> generarReportePorSucursal();

    VentaResponse obtenerPorId(Long id);
}