package com.duoc.tiendamascotas.service;

import com.duoc.tiendamascotas.dto.DetalleEnvioProductoDTO;
import com.duoc.tiendamascotas.dto.EnvioDTO;
import com.duoc.tiendamascotas.dto.ProductoDTO;
import com.duoc.tiendamascotas.entities.DetalleEnvioProductoEntity;
import com.duoc.tiendamascotas.entities.EnvioEntity;
import com.duoc.tiendamascotas.entities.ProductoEntity;
import com.duoc.tiendamascotas.exceptions.EnvioNotFoundException;
import com.duoc.tiendamascotas.exceptions.IllegalNumberException;
import com.duoc.tiendamascotas.mapper.EnvioMapper;
import com.duoc.tiendamascotas.repositories.DetalleEnvioRepository;
import com.duoc.tiendamascotas.repositories.EnvioRepository;
import com.duoc.tiendamascotas.repositories.ProductoRepository;
import com.duoc.tiendamascotas.services.EnvioProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EnvioProductoServiceImplTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private DetalleEnvioRepository detalleEnvioRepository;

    @Mock
    private EnvioMapper envioMapper;

    @InjectMocks
    private EnvioProductoServiceImpl envioProductoService;

    private EnvioEntity envioEntity;
    private EnvioDTO envioDTO;

    private ProductoEntity productoEntity;
    private ProductoDTO productoDTO;

    private DetalleEnvioProductoEntity detalleEnvioProductoEntity;
    private DetalleEnvioProductoDTO detalleEnvioProductoDTO;

    @BeforeEach
    public void setUp() {
        // Inicializa los mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);

        envioEntity = EnvioEntity.builder()
                .ubicacionActual("ubicacion test entity")
                .destino("destino test entity")
                .idEstadoEnvio(1)
                .build();

        envioDTO = EnvioDTO.builder()
                .ubicacionActual("ubicacion test dto")
                .destino("destino test dto")
                .idEstadoEnvio(1)
                .build();

        productoEntity = ProductoEntity.builder()
                .nombre("nombre test entity")
                .precio(1000)
                .stock(10)
                .build();

        productoDTO = ProductoDTO.builder()
                .nombre("nombre test dto")
                .precio(1000)
                .stock(10)
                .build();

        detalleEnvioProductoEntity = DetalleEnvioProductoEntity.builder()
                .idEnvio(1)
                .idProducto(1)
                .build();

        detalleEnvioProductoDTO = DetalleEnvioProductoDTO.builder()
                .idEnvio(1)
                .idProducto(1)
                .build();
    }

    @Test
    public void generarEnvioTest() {
        // Crear una lista de productos
        ProductoDTO producto1 = ProductoDTO.builder()
                .idProducto(1)
                .build();

        ProductoDTO producto2 = ProductoDTO.builder()
                .idProducto(2)
                .build();

        // Almacenar productos en lista
        envioDTO.setListaProducto(Arrays.asList(producto1, producto2));

        when(envioRepository.save(any(EnvioEntity.class))).thenReturn(envioEntity);
        when(detalleEnvioRepository.save(any(DetalleEnvioProductoEntity.class))).thenReturn(null);

        // Ejecutar método a probar del servicio
        assertDoesNotThrow(() -> envioProductoService.generarEnvio(envioDTO));

        verify(envioRepository).save(any(EnvioEntity.class));

        verify(detalleEnvioRepository, times(2)).save(any(DetalleEnvioProductoEntity.class));

    }

    @Test
    public void generarEnvioExcepcionTest() {
        // Simular que el repository lanza una excepción
        when(envioRepository.save(any(EnvioEntity.class))).thenThrow(new RuntimeException("No se pudo crear el envio"));

        // Verificar que el método lanza una excepción tipo RuntimeException
        assertThrows(RuntimeException.class, () -> envioProductoService.generarEnvio(envioDTO));
    }

    @Test
    public void modificarEstadoEnvioTest() {
        when(envioRepository.findById(anyInt())).thenReturn(Optional.ofNullable(envioEntity));
        when(envioRepository.save(any(EnvioEntity.class))).thenReturn(envioEntity);

        // Ejecutar el método a probar
        assertDoesNotThrow(() -> envioProductoService.modificarEstadoEnvio(1, 2));

        // Verificar que el estado del envío fue actualizado
        assertEquals(2, envioEntity.getIdEstadoEnvio());

        // Verificar que el método save fue llamado en envioRepository
        verify(envioRepository).save(envioEntity);
    }

    @Test
    public void modificarEstadoEnvioExcepcionTest() {
        when(envioRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EnvioNotFoundException.class,
                () -> envioProductoService.modificarEstadoEnvio(1, 2));

        verify(envioRepository, never()).save(any(EnvioEntity.class));
    }

    @Test
    public void obtenerEnviosTest() {
        EnvioEntity envioEntity1 = EnvioEntity.builder()
                .ubicacionActual("ubicacion test 1")
                .destino("destino test 1")
                .idEstadoEnvio(1)
                .build();

        EnvioEntity envioEntity2 = EnvioEntity.builder()
                .ubicacionActual("ubicacion test 2")
                .destino("destino test 2")
                .idEstadoEnvio(3)
                .build();

        List<EnvioEntity> listaEnvioEntity = Arrays.asList(envioEntity1, envioEntity2);

        // Crear los DTOs correspondientes
        EnvioDTO envioDTO1 = EnvioDTO.builder()
                .ubicacionActual("ubicacion test 1")
                .destino("destino test 1")
                .idEstadoEnvio(1)
                .build();

        EnvioDTO envioDTO2 = EnvioDTO.builder()
                .ubicacionActual("ubicacion test 2")
                .destino("destino test 2")
                .idEstadoEnvio(3)
                .build();

        when(envioRepository.findAll()).thenReturn(listaEnvioEntity);
        when(envioMapper.envioEntityToDTO(envioEntity1)).thenReturn(envioDTO1);
        when(envioMapper.envioEntityToDTO(envioEntity2)).thenReturn(envioDTO2);

        List<EnvioDTO> resultado = envioProductoService.obtenerEnvios();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("ubicacion test 2", resultado.get(0).getUbicacionActual());
        assertEquals("ubicacion test 2", resultado.get(1).getUbicacionActual());

        // Verificar que envioRepository ha llamado el método findAll()
        verify(envioRepository).findAll();
    }

    @Test
    public void consultarEnvioByIdTest() {
        EnvioEntity envioEntity1 = EnvioEntity.builder()
                .idEnvio(1)
                .ubicacionActual("ubicacion test")
                .destino("destino test")
                .idEstadoEnvio(1)
                .build();

        DetalleEnvioProductoEntity detalleEnvioProductoEntity1 = DetalleEnvioProductoEntity.builder()
                .idEnvio(1)
                .idProducto(1)
                .build();


        List<DetalleEnvioProductoEntity> listaDetallesEntities = List.of(detalleEnvioProductoEntity1);

        ProductoDTO productoDTO1 = ProductoDTO.builder()
                .idProducto(1)
                .build();

        when(envioRepository.findById(anyInt())).thenReturn(Optional.ofNullable(envioEntity1));
        when(detalleEnvioRepository.findAllByIdEnvio(anyInt())).thenReturn(listaDetallesEntities);
        when(productoRepository.findById(1)).thenReturn(Optional.of(new ProductoEntity()));
        when(envioMapper.productoEntityToDTO(any())).thenReturn(productoDTO1);

        // Verificar llamando al método del servicio
        Optional<EnvioDTO> resultado = envioProductoService.consultarEnvioById(1);

        assertTrue(resultado.isPresent());
        assertNotNull(resultado);
        assertEquals(1, resultado.get().getIdEnvio());
        assertEquals("ubicacion test", resultado.get().getUbicacionActual());
        assertEquals("destino test", resultado.get().getDestino());
        assertEquals(1, resultado.get().getListaProducto().size());

    }

    @Test
    public void consultarEnvioByIdExcepcionTest() {
        when(envioRepository.findById(-1)).thenThrow(new IllegalNumberException("El id de envio debe ser positivo y no nulo"));
        when(envioRepository.findById(anyInt())).thenThrow(new EnvioNotFoundException("Envio no encontrado"));

        // Verificar que el método lanza una excepción tipo RuntimeException
        assertThrows(IllegalNumberException.class, () -> envioProductoService.consultarEnvioById(-1));
        assertThrows(EnvioNotFoundException.class, () -> envioProductoService.consultarEnvioById(1));
    }

    @Test
    public void consultarUbicacionTest() {
        EnvioEntity envioEntity1 = EnvioEntity.builder()
                .idEnvio(1)
                .ubicacionActual("ubicacion test")
                .build();

        EnvioDTO envioDTO1 = EnvioDTO.builder()
                .ubicacionActual("ubicacion test")
                .build();

        when(envioRepository.findById(anyInt())).thenReturn(Optional.ofNullable(envioEntity1));
        when(envioMapper.envioEntityToDTO(envioEntity1)).thenReturn(envioDTO1);

        String resultado = envioProductoService.consultarUbicacion(1);

        assertNotNull(resultado);
        assertEquals("ubicacion test", resultado);
    }

    @Test
    public void consultarUbicacionExcepcionTest() {

        when(envioRepository.findById(-1)).thenThrow(new IllegalNumberException("El ID del envío debe ser positivo y no nulo"));
        when(envioRepository.findById(anyInt())).thenThrow(new EnvioNotFoundException("No se han encontrado datos del envío"));

        assertThrows(IllegalNumberException.class, () -> envioProductoService.consultarUbicacion(-1));
        assertThrows(EnvioNotFoundException.class, () -> envioProductoService.consultarUbicacion(1));
    }

    @Test
    public void eliminarEnvioTest() {

        EnvioEntity envioEntity1 = EnvioEntity.builder()
                .idEnvio(1)
                .ubicacionActual("ubicacion test")
                .build();

        when(envioRepository.findById(anyInt())).thenReturn(Optional.ofNullable(envioEntity1));

        String resultado = envioProductoService.eliminarEnvio(1);

        verify(detalleEnvioRepository).deleteAllByIdEnvio(1);
        verify(envioRepository).deleteById(1);
        assertEquals("Envio eliminado exitosamente", resultado);


    }

    @Test
    public void eliminarEnvioExcepcionTest() {

        when(envioRepository.findById(-1)).thenThrow(new IllegalNumberException("El ID del envío debe ser positivo y no nulo"));
        when(envioRepository.findById(anyInt())).thenThrow(new EnvioNotFoundException("El id no existe en los registros"));

        assertThrows(IllegalNumberException.class, () -> envioProductoService.eliminarEnvio(-1));
        assertThrows(EnvioNotFoundException.class, () -> envioProductoService.eliminarEnvio(1));

    }
}
