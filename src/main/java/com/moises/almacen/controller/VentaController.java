package com.moises.almacen.controller;

import com.moises.almacen.dto.ventas.VentaRequest;
import com.moises.almacen.dto.ventas.VentaResponse;
import com.moises.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.moises.almacen.services.Ventas.VentaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@AllArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar() {
        return ResponseEntity.ok(ventaService.listarActivas());
    }

    @GetMapping("/canceladas")
    public ResponseEntity<List<VentaResponse>> listarCanceladas() {
        return ResponseEntity.ok(ventaService.listarCanceladas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerPorId(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }

    //  POST /api/ventas - Registrar nueva venta
    @PostMapping
    public ResponseEntity<VentaResponse> registrar(
            @Valid @RequestBody VentaRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ventaService.registrar(request));
    }


    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<VentaResponse> cancelar(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(ventaService.cancelar(id));
    }

    @GetMapping("/reporte-sucursal")
    public ResponseEntity<List<ReporteVentasSucursalResponse>> obtenerReportePorSucursal() {
        return ResponseEntity.ok(ventaService.generarReportePorSucursal());
    }
}