package com.moises.almacen.services.productos;

import com.moises.almacen.dto.productos.ProductoRequest;
import com.moises.almacen.dto.productos.ProductosResponse;

import java.util.List;


public interface ProductoService {
    List<ProductosResponse> listar();
    ProductosResponse obtenerPorId(Long id);
    ProductosResponse registrar(ProductoRequest request);
    ProductosResponse actualizar(ProductoRequest request, Long id);

    void eliminar(Long id);
}
