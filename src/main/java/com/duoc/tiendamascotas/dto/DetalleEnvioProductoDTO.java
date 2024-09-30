package com.duoc.tiendamascotas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetalleEnvioProductoDTO {

    @NotNull(message = "El campo idDetalleEnvioProd no puede estar vacío")
    private Integer idDetalleEnvioProd;

    @NotNull(message = "El campo idEnvio no puede estar vacío")
    private Integer idEnvio;

    @NotNull(message = "El campo idProducto no puede estar vacío")
    private Integer idProducto;

}
