package com.moises.almacen.mappers;


import com.moises.almacen.dto.sucursales.SucursalesRequest;
import com.moises.almacen.dto.sucursales.SucursalesResponse;
import com.moises.almacen.entities.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {

    public Sucursal requestAEntidad(SucursalesRequest request){
        if (request == null) return null;

        return Sucursal.builder()
                .nombre(request.nombre().trim())
                .direccion(request.direccion())
                .build();
    }

    public SucursalesResponse entidadAResponse(Sucursal sucursal){
        if (sucursal == null) return null;

        return new SucursalesResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion()
        );
    }
}
