package com.duoc.tiendamascotas.entities;
import com.duoc.tiendamascotas.dto.ProductoDTO;
import jakarta.persistence.*;
import lombok.Builder;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Builder
@Entity
@Table(name = "envio")
public class EnvioEntity extends RepresentationModel<ProductoEntity>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envio")
    private Integer idEnvio;

    @Column(name = "ubicacion_actual")
    private String ubicacionActual;

    @Column(name = "destino")
    private String destino;

    @Column(name = "id_estado_envio")
    private Integer idEstadoEnvio;

    public EnvioEntity() {
    }

    public EnvioEntity(Integer idEnvio, String ubicacionActual, String destino, Integer idEstadoEnvio) {
        this.idEnvio = idEnvio;
        this.ubicacionActual = ubicacionActual;
        this.destino = destino;
        this.idEstadoEnvio = idEstadoEnvio;
    }

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getUbicacionActual() {
        return ubicacionActual;
    }

    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getIdEstadoEnvio() {
        return idEstadoEnvio;
    }

    public void setIdEstadoEnvio(Integer idEstadoEnvio) {
        this.idEstadoEnvio = idEstadoEnvio;
    }
}
