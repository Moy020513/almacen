package com.moises.almacen.services.productos;

import com.moises.almacen.dto.productos.ProductoRequest;
import com.moises.almacen.dto.productos.ProductosResponse;

import java.math.BigDecimal;
import java.util.List;


public interface ProductoService {
    List<ProductosResponse> listar(
            String nombre, String categoria,
            BigDecimal precioMin, BigDecimal precioMax
    );
    ProductosResponse obtenerPorId(Long id);
    ProductosResponse registrar(ProductoRequest request);
    ProductosResponse actualizar(ProductoRequest request, Long id);

    void eliminar(Long id);
}
