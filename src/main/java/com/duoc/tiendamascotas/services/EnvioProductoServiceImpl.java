package com.duoc.tiendamascotas.services;

import com.duoc.tiendamascotas.dto.EnvioDTO;
import com.duoc.tiendamascotas.dto.ProductoDTO;
import com.duoc.tiendamascotas.entities.DetalleEnvioProductoEntity;
import com.duoc.tiendamascotas.entities.EnvioEntity;
import com.duoc.tiendamascotas.exceptions.EnvioNotFoundException;
import com.duoc.tiendamascotas.exceptions.IllegalNumberException;
import com.duoc.tiendamascotas.mapper.EnvioMapper;
import com.duoc.tiendamascotas.repositories.DetalleEnvioRepository;
import com.duoc.tiendamascotas.repositories.EnvioRepository;
import com.duoc.tiendamascotas.repositories.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class EnvioProductoServiceImpl implements EnvioProductoService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DetalleEnvioRepository detalleEnvioRepository;

    @Autowired
    private EnvioMapper envioMapper;

    @Override
    public EnvioDTO generarEnvio(EnvioDTO envioDTO) {

        try {
            EnvioEntity envioEntityGuardado = envioRepository.save(EnvioEntity.builder()
                            .ubicacionActual(envioDTO.getUbicacionActual())
                            .destino(envioDTO.getDestino())
                            .idEstadoEnvio(envioDTO.getIdEstadoEnvio())
                            .build());
            envioDTO.getListaProducto().forEach(producto -> {
                DetalleEnvioProductoEntity detalleEnvioProductoEntity = detalleEnvioRepository.save(DetalleEnvioProductoEntity.builder()
                                .idEnvio(envioEntityGuardado.getIdEnvio())
                                .idProducto(producto.getIdProducto())
                        .build());
            });
            envioDTO.setIdEnvio(envioEntityGuardado.getIdEnvio());
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el envio");
        }
        return envioDTO;
    }

    @Override
    public Optional<EnvioDTO> modificarEstadoEnvio(int idEnvio, int idEstadoEnvio) {
        Optional<EnvioEntity> envioOptional = envioRepository.findById(idEnvio);

        // Verificar si el envío existe
        if (envioOptional.isPresent()) {
            EnvioEntity envioEntity = envioOptional.get();

            // Modificar el estado del envío
            envioEntity.setIdEstadoEnvio(idEstadoEnvio);

            // Guardar el envío modificado
            envioRepository.save(envioEntity);

            // Convertir a DTO y devolver
            return Optional.ofNullable(envioMapper.envioEntityToDTO(envioEntity));
        } else {
            // Lanzar excepción si el envío no se encuentra
            throw new EnvioNotFoundException("Id envio ingresado: " + idEnvio + ", no está en los registros");
        }
    }

    @Override
    public List<EnvioDTO> obtenerEnvios() {
        List<EnvioEntity> envios = envioRepository.findAll();

        if (envios.isEmpty()) {
            throw new EnvioNotFoundException("No se encontraron envios");
        }
        return envios.stream()
                .map(envioEntity -> envioMapper.envioEntityToDTO(envioEntity))
                .toList();
    }

    @Override
    public Optional<EnvioDTO> consultarEnvioById(int id) {

        if(id <= 0) {
            throw new IllegalNumberException("El id de envio debe ser positivo y no nulo");
        }

        Optional<EnvioEntity> envio = envioRepository.findById(id);

        if(envio.isPresent()){
            List<DetalleEnvioProductoEntity> listaDetalles = detalleEnvioRepository.findAllByIdEnvio(id);

            List<ProductoDTO> listaProductos = new ArrayList<>();
            listaDetalles.forEach(detalleEnvioProd -> {
                ProductoDTO productoDTO = envioMapper.productoEntityToDTO(productoRepository.findById(detalleEnvioProd.getIdProducto()).get());
                listaProductos.add(productoDTO);
            });

            return Optional.ofNullable(EnvioDTO.builder()
                    .idEnvio(envio.get().getIdEnvio())
                    .listaProducto(listaProductos)
                    .ubicacionActual(envio.get().getUbicacionActual())
                    .destino(envio.get().getDestino())
                    .idEstadoEnvio(envio.get().getIdEstadoEnvio())
                    .build());
        } else {
            throw new EnvioNotFoundException("Envio no encontrado con id: " + id);
        }
    }

    public String consultarUbicacion(int idEnvio) {
        // Valida que el ID sea positivo
        if (idEnvio <= 0) {
            throw new IllegalNumberException("El ID del envío debe ser positivo y no nulo");
        }

        // Busca el envío en el repositorio
        EnvioEntity envioEntity = envioRepository.findById(idEnvio)
                .orElseThrow(() -> new EnvioNotFoundException("No se han encontrado datos del envío con ID: " + idEnvio));

        // Obtiene la ubicación actual del envío
        String ubicacionActual = envioMapper.envioEntityToDTO(envioEntity).getUbicacionActual();

        // Verifica si la ubicación está vacía
        if (ubicacionActual == null || ubicacionActual.isEmpty()) {
            throw new EnvioNotFoundException("No se ha encontrado la ubicación actual del envío con id: " + idEnvio);
        }

        return ubicacionActual;
    }

    @Override
    public String eliminarEnvio(int idEnvio) {

        if (idEnvio <= 0) {
            throw new IllegalNumberException("El ID del envio debe ser positivo y no nulo");
        }

        EnvioEntity envioEntity = envioRepository.findById(idEnvio)
                .orElseThrow(() -> new EnvioNotFoundException("El id de envio: " +  idEnvio +  ", no existe en los registros."));

        detalleEnvioRepository.deleteAllByIdEnvio(idEnvio);
        envioRepository.deleteById(idEnvio);
        return "Envio eliminado exitosamente";
    }

}
