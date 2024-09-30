package com.duoc.tiendamascotas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoDTO {

    @NotNull(message = "El campo idProducto no puede estar vacío")
    private Integer idProducto;

    @NotNull(message = "El campo nombre no puede estar vacío")
    @Size(min = 5, max = 100, message = "El campo nombre debe tener entre 5 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El campo precio no puede estar vacío")
    private int precio;

    @NotNull(message = "El campo stock no puede estar vacío")
    private int stock;

}
