package com.duoc.tiendamascotas.mapper;

import com.duoc.tiendamascotas.dto.DetalleEnvioProductoDTO;
import com.duoc.tiendamascotas.dto.EnvioDTO;
import com.duoc.tiendamascotas.dto.ProductoDTO;
import com.duoc.tiendamascotas.entities.DetalleEnvioProductoEntity;
import com.duoc.tiendamascotas.entities.EnvioEntity;
import com.duoc.tiendamascotas.entities.ProductoEntity;
import org.springframework.stereotype.Component;

@Component
public class EnvioMapper {

    // ENTRA ENTITY Y SALE DTO
    public EnvioDTO envioEntityToDTO(EnvioEntity envioEntity) {
        return EnvioDTO.builder()
                .idEnvio(envioEntity.getIdEnvio())
                .ubicacionActual(envioEntity.getUbicacionActual())
                .destino(envioEntity.getDestino())
                .idEstadoEnvio(envioEntity.getIdEstadoEnvio())
                .build();
    }

    public ProductoDTO productoEntityToDTO(ProductoEntity productoEntity) {
        return ProductoDTO.builder()
                .idProducto(productoEntity.getIdProducto())
                .nombre(productoEntity.getNombre())
                .precio(productoEntity.getPrecio())
                .stock(productoEntity.getStock())
                .build();
    }

    public DetalleEnvioProductoDTO detalleEnvioProdEntityToDTO(DetalleEnvioProductoEntity detalleEnvioProductoEntity) {
        return DetalleEnvioProductoDTO.builder()
                .idDetalleEnvioProd(detalleEnvioProductoEntity.getIdDetalleEnvioProd())
                .idEnvio(detalleEnvioProductoEntity.getIdEnvio())
                .idProducto(detalleEnvioProductoEntity.getIdProducto())
                .build();
    }


    // ENTRA DTO Y SALE ENTITY
    public EnvioEntity envioDtoToEntity(EnvioDTO envioDTO) {
        return EnvioEntity.builder()
                .idEnvio(envioDTO.getIdEnvio())
                .ubicacionActual(envioDTO.getUbicacionActual())
                .destino(envioDTO.getDestino())
                .build();
    }

    public ProductoEntity productoDtoToEntity(ProductoDTO productoDTO) {
        return ProductoEntity.builder()
                .idProducto(productoDTO.getIdProducto())
                .nombre(productoDTO.getNombre())
                .precio(productoDTO.getPrecio())
                .stock(productoDTO.getStock())
                .build();
    }

    public DetalleEnvioProductoEntity detalleEnvioProdDtoToEntity(DetalleEnvioProductoDTO detalleEnvioProductoDTO) {
        return DetalleEnvioProductoEntity.builder()
                .idDetalleEnvioProd(detalleEnvioProductoDTO.getIdDetalleEnvioProd())
                .idEnvio(detalleEnvioProductoDTO.getIdEnvio())
                .idProducto(detalleEnvioProductoDTO.getIdProducto())
                .build();
    }

}
