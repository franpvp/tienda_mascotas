package com.duoc.tiendamascotas.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EnvioDTO {

    @Positive(message = "El campo idEnvio debe ser un número positivo")
    private Integer idEnvio;

    @NotNull(message = "El campo listaProducto no puede estar vacío")
    private List<ProductoDTO> listaProducto;

    @NotNull(message = "El campo ubicacionActual no puede estar vacío")
    @Size(min = 5, max = 100, message = "El campo ubicacionActual debe tener entre 5 y 100 caracteres")
    private String ubicacionActual;

    @NotNull(message = "El campo destino no puede estar vacío")
    @Size(min = 10, max = 100, message = "El campo destino debe tener entre 10 y 100 caracteres")
    private String destino;

    @NotNull(message = "El campo idEstadoEnvio no puede estar vacío")
    @Positive(message = "El campo idEstadoEnvio debe ser un número positivo")
    private Integer idEstadoEnvio;

}
