package com.duoc.tiendamascotas.repositories;

import com.duoc.tiendamascotas.entities.DetalleEnvioProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetalleEnvioRepository extends JpaRepository<DetalleEnvioProductoEntity, Integer> {

    List<DetalleEnvioProductoEntity> findAllByIdEnvio(int id);

    @Modifying
    @Query("DELETE FROM DetalleEnvioProductoEntity d WHERE d.idEnvio = :idEnvio")
    void deleteAllByIdEnvio(@Param("idEnvio") Integer idEnvio);

}
