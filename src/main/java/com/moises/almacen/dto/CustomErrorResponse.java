package com.moises.almacen.dto;

public record CustomErrorResponse(
        int codigo,
        String mansaje
) {}
