package com.moises.almacen.services.sucursales;

import com.moises.almacen.dto.sucursales.SucursalesRequest;
import com.moises.almacen.dto.sucursales.SucursalesResponse;
import com.moises.almacen.entities.Sucursal;
import com.moises.almacen.exceptions.RecursoNoEncontradoException;
import com.moises.almacen.mappers.SucursalMapper;
import com.moises.almacen.repositories.SucursalRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class SucursalServiceImpl implements SucursalService{

    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;

    @Override
    public List<SucursalesResponse> listar() {
        log.info("Listando todas las sucursales");
        return sucursalRepository.findAll().stream()
                .map(sucursalMapper::entidadAResponse).toList();
    }

    @Override
    public SucursalesResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursalOException(id));
    }

    @Override
    public SucursalesResponse registrar(SucursalesRequest request) {
        log.info("Registrando nueva sucursal...");
        validarDatosUnicos(request);
        Sucursal sucursal = sucursalMapper.requestAEntidad(request);
        sucursalRepository.save(sucursal);
        log.info("Nueva sucursal {} registrada", sucursal.getNombre());
        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public SucursalesResponse actualizar(SucursalesRequest request, Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);
        log.info("Actualizando sucursal con id: {}", id);
        validarCambiosUnicos(request, id);

        sucursal.actualizar(
                request.nombre(),
                request.direccion());

        log.info("Sucursal con id {} actualizada", id);
        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);
        log.info("Eliminando sucursal con id {}", id);
        sucursalRepository.delete(sucursal);
        log.info("Sucursal con id {} eliminada", id);

    }


    private Sucursal obtenerSucursalOException(Long id){
        log.info("Buscando sucursal con id: {}", id);
        return sucursalRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Sucursal no entonctrada id:" + id));
    }

    private void validarDatosUnicos(SucursalesRequest request){
        log.info("Validando nombre único");
        if (sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre de:" + request.nombre());
    }
    private void validarCambiosUnicos(SucursalesRequest request, Long id){
        log.info("Validando cambio de nombre único");
        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre de:" + request.nombre());
    }
}
