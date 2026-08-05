package com.moises.almacen.services.productos;

import com.moises.almacen.dto.productos.ProductoRequest;
import com.moises.almacen.dto.productos.ProductosResponse;
import com.moises.almacen.entities.Producto;
import com.moises.almacen.enums.Categoria;
import com.moises.almacen.exceptions.RecursoNoEncontradoException;
import com.moises.almacen.mappers.ProductoMapper;
import com.moises.almacen.repositories.ProductoRespository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@AllArgsConstructor
@Transactional //Rollback automático
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRespository productoRepository;
    private final ProductoMapper productoMapper;
    @Override
    @Transactional(readOnly = true)
    public List<ProductosResponse> listar() {
        log.info("Listando todos los productos");
        return productoRepository.findAll().stream()
                .map(productoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductosResponse obtenerPorId(Long id) {

        return productoMapper.entidadAResponse(obtenerProductoOException(id));
    }

    @Override
    public ProductosResponse registrar(ProductoRequest request) {
        log.info("Registrando nuevo producto...");
        Categoria categoria = obtenerCategoriaPorDescripcion(request.categoria());
        Producto producto = productoMapper.requestAEntidad(request, categoria);
        productoRepository.save(producto);
        log.info("Nuevo producto {} registrado", producto.getNombre());
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public ProductosResponse actualizar(ProductoRequest request, Long id) {
        Producto producto = obtenerProductoOException(id);
        Categoria categoria = obtenerCategoriaPorDescripcion(request.categoria());
        log.info("Actualizando producto con id: {}", id);

        producto.actualizar(
                request.nombre(),
                categoria,
                request.precio(),
                request.cantidad());

        log.info("Producto con id {} actualizado", id);
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerProductoOException(id);
        log.info("Eliminando producto con id {}", id);
        productoRepository.delete(producto);
        log.info("Producto con id {} eliminado", id);
    }
    private Producto obtenerProductoOException(Long id){
        log.info("Buscando producto con id: {}", id);
        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Producto no encontrado co id:" + id));
    }

    private Categoria obtenerCategoriaPorDescripcion(String descripcion) {
        return Categoria.obtenerCategoriaPorDescripcion(descripcion.trim());
    }
}
