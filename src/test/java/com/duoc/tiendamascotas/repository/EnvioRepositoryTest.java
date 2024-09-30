package com.duoc.tiendamascotas.repository;

import com.duoc.tiendamascotas.dto.EnvioDTO;
import com.duoc.tiendamascotas.entities.EnvioEntity;
import com.duoc.tiendamascotas.repositories.DetalleEnvioRepository;
import com.duoc.tiendamascotas.repositories.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EnvioRepositoryTest {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private DetalleEnvioRepository detalleEnvioRepository;

    private EnvioDTO envioDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        EnvioEntity envioEntity = EnvioEntity.builder()
                .idEnvio(1)
                .ubicacionActual("ubicacion 1")
                .destino("destino 1")
                .idEstadoEnvio(1)
                .build();

        envioDTO = EnvioDTO.builder()
                .idEnvio(1)
                .ubicacionActual("ubicacion 1")
                .destino("destino 1")
                .idEstadoEnvio(1)
                .build();

    }

    @Test
    public void generarEnvioTest() {
        EnvioEntity envioEntityGuardado = envioRepository.save(EnvioEntity.builder()
                .idEnvio(envioDTO.getIdEnvio())
                .ubicacionActual(envioDTO.getUbicacionActual())
                .destino(envioDTO.getDestino())
                .idEstadoEnvio(envioDTO.getIdEstadoEnvio())
                .build());

        assertNotNull(envioEntityGuardado.getIdEnvio(), "El ID de la película no debe ser nulo");
        assertEquals("ubicacion 1", envioEntityGuardado.getUbicacionActual());
        assertEquals("destino 1", envioEntityGuardado.getDestino());
        assertEquals(1, envioEntityGuardado.getIdEnvio());

    }

    @Test
    public void actualizarEnvioByIdTest() {
        int idEnvio = 1;
        int idEstadoEnvio = 2;

        Optional<EnvioEntity> envioOptional = envioRepository.findById(idEnvio);
        assertTrue(envioOptional.isPresent());

        EnvioEntity envioEntity = envioOptional.get();
        envioEntity.setIdEstadoEnvio(idEstadoEnvio);

        EnvioEntity envioGuardado = envioRepository.save(envioEntity);
        assertEquals(idEstadoEnvio, envioGuardado.getIdEstadoEnvio());
    }

    @Test
    public void obtenerEnviosTest() {

        List<EnvioEntity> listaEnvios = envioRepository.findAll();

        assertEquals(5, listaEnvios.size());
        assertEquals("Santiago", listaEnvios.getFirst().getUbicacionActual());
        assertEquals("La Serena", listaEnvios.getFirst().getDestino());
        assertEquals(1, listaEnvios.getFirst().getIdEstadoEnvio());

    }

    @Test
    public void obtenerEnvioByIdTest() {
        int idEnvio = 1;
        Optional<EnvioEntity> envioEntity = envioRepository.findById(idEnvio);

        assertEquals(1, envioEntity.get().getIdEnvio());
        assertEquals("Santiago", envioEntity.get().getUbicacionActual());
        assertEquals("La Serena", envioEntity.get().getDestino());
        assertEquals(1, envioEntity.get().getIdEstadoEnvio());

    }

    @Test
    public void eliminarEnvioByIdTest() {
        int idEnvio = 1;

        detalleEnvioRepository.deleteAllByIdEnvio(idEnvio);
        envioRepository.deleteById(idEnvio);
        assertFalse(envioRepository.existsById(idEnvio));
    }

}
