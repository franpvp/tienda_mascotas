package com.duoc.tiendamascotas.repositories;

import com.duoc.tiendamascotas.entities.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {
}
