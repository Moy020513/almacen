package com.moises.almacen.mappers;

import com.moises.almacen.dto.productos.ProductoRequest;
import com.moises.almacen.dto.productos.ProductosResponse;
import com.moises.almacen.entities.Producto;
import com.moises.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {
    public Producto requestAEntidad(ProductoRequest request, Categoria categoria){
        if (request == null) return null;

        return Producto.builder()
                .nombre(request.nombre().trim())
                .categoria(categoria)
                .precio(request.precio())
                .cantidad(request.cantidad())
                .build();
    }

    public ProductosResponse entidadAResponse(Producto producto){
        if (producto == null) return null;

        return new ProductosResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getDescripcion(),
                producto.getPrecio(),
                producto.getCantidad()
        );
    }
}
