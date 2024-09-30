package com.duoc.tiendamascotas.entities;

import jakarta.persistence.*;
import lombok.Builder;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Entity
@Table(name = "detalle_envio_prod")
public class DetalleEnvioProductoEntity extends RepresentationModel<DetalleEnvioProductoEntity>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_envio_prod")
    private Integer idDetalleEnvioProd;

    @Column(name = "id_envio")
    private Integer idEnvio;

    @Column(name = "id_producto")
    private Integer idProducto;

    public DetalleEnvioProductoEntity() {

    }

    public DetalleEnvioProductoEntity(Integer idDetalleEnvioProd, Integer idEnvio, Integer idProducto) {
        this.idDetalleEnvioProd = idDetalleEnvioProd;
        this.idEnvio = idEnvio;
        this.idProducto = idProducto;
    }

    public Integer getIdDetalleEnvioProd() {
        return idDetalleEnvioProd;
    }

    public void setIdDetalleEnvioProd(Integer idDetalleEnvioProd) {
        this.idDetalleEnvioProd = idDetalleEnvioProd;
    }

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }
}
