package com.moises.almacen.services.sucursales;

import com.moises.almacen.dto.sucursales.SucursalesRequest;
import com.moises.almacen.dto.sucursales.SucursalesResponse;

import java.util.List;

public interface SucursalService {
    List<SucursalesResponse> listar();
    SucursalesResponse obtenerPorId(Long id);
    SucursalesResponse registrar(SucursalesRequest request);
    SucursalesResponse actualizar(SucursalesRequest request, Long id);

    void eliminar(Long id);
}
