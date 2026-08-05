package com.moises.almacen.controller;

import com.moises.almacen.dto.sucursales.SucursalesRequest;
import com.moises.almacen.dto.sucursales.SucursalesResponse;
import com.moises.almacen.services.sucursales.SucursalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@AllArgsConstructor
@Validated
public class SucursalController {
    private final SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalesResponse>> listar(){
        return ResponseEntity.ok(sucursalService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalesResponse> obtenerPorId(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ){
        return ResponseEntity.ok(sucursalService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<SucursalesResponse> registrar(
            @Valid @RequestBody SucursalesRequest request
    ){
        return  ResponseEntity.status(HttpStatus.CREATED).body(sucursalService.registrar(request));
    }

    @PutMapping
    public ResponseEntity<SucursalesResponse> actualizar(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,

            @Valid @RequestBody SucursalesRequest request
    ){
        return  ResponseEntity.ok(sucursalService.actualizar(request, id));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ){
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
