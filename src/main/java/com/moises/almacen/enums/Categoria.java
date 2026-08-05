package com.moises.almacen.enums;

import com.moises.almacen.exceptions.RecursoNoEncontradoException;
import com.moises.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Categoria {
    ALIMENTO("Alimento"),
    HIGIENE("Higiene"),
    JUGUETE("Juguete"),
    ELECTRONICA("electrónica"),
    ROPA("Ropa"),
    ACCESORIO("Accesorio"),
    FARMACIA("Farmacia");

    private final String descripcion;

    public static Categoria obtenerCategoriaPorDescripcion(String descripcion){

        StringCustomUtils.validarNoVacio(descripcion, "La descripción es requerida");
        String descripcionNormalizada = StringCustomUtils.quitarAcentos(descripcion);

        for (Categoria categoria : values()) {
            if (StringCustomUtils.quitarAcentos(categoria.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return categoria;
        }
        throw new RecursoNoEncontradoException("No existe una categoría con la descripción:" + descripcion);
    }
}
