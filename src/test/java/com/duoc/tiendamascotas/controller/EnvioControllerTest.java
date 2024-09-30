package com.duoc.tiendamascotas.controller;

import com.duoc.tiendamascotas.controllers.EnvioController;
import com.duoc.tiendamascotas.dto.*;
import com.duoc.tiendamascotas.entities.DetalleEnvioProductoEntity;
import com.duoc.tiendamascotas.entities.EnvioEntity;
import com.duoc.tiendamascotas.entities.ProductoEntity;
import com.duoc.tiendamascotas.services.EnvioProductoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(EnvioController.class)
public class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioProductoServiceImpl envioProductoService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EnvioController envioController;

    private ProductoDTO productoDTO;

    private EnvioDTO envioDTO;

    private DetalleEnvioProductoDTO detalleEnvioProductoDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void generarEnvioTest() throws Exception {

        EnvioDTO envioDTO1 = EnvioDTO.builder()
                .idEnvio(1)
                .ubicacionActual("ubicación 1")
                .destino("destino 1")
                .idEstadoEnvio(1)
                .build();

        // Simulación del servicio
        when(envioProductoService.generarEnvio(any(EnvioDTO.class))).thenReturn(envioDTO1);

        // Ejecutar el test
        mockMvc.perform(post("/api/envio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ubicacionActual\": \"ubicación 1\", \"destino\": \"destino 1\", \"idEstadoEnvio\": 1}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                // Verificar que los enlaces HATEOAS están presentes en la respuesta
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.allEnvios.href").exists());


        // Verificar que el servicio fue llamado una vez
        verify(envioProductoService, times(1)).generarEnvio(any(EnvioDTO.class));
    }

    @Test
    public void modificarEstadoEnvioTest() throws Exception {
        int idEnvio = 1;
        int idEstadoEnvio = 2;

        // Simulación del DTO de envío
        EnvioDTO envioDTO1 = EnvioDTO.builder()
                .idEnvio(idEnvio)
                .idEstadoEnvio(idEstadoEnvio)
                .build();

        // Simulación del servicio
        when(envioProductoService.modificarEstadoEnvio(idEnvio, idEstadoEnvio)).thenReturn(Optional.ofNullable(envioDTO1));

        // Ejecutar el test
        mockMvc.perform(put("/api/envio/modificar-estado/{id-envio}/{id-estado}", idEnvio, idEstadoEnvio)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verificar los enlaces HATEOAS en la respuesta
                .andExpect(jsonPath("$._links.self.href").exists()) // Verificar que el enlace 'self' existe
                .andExpect(jsonPath("$._links.allEnvios.href").exists()); // Verificar que el enlace 'allEnvios' existe

        // Verificar que el servicio fue llamado una vez con los parámetros correctos
        verify(envioProductoService, times(1)).modificarEstadoEnvio(idEnvio, idEstadoEnvio);
    }

    @Test
    public void obtenerEnviosTest() throws Exception {
        EnvioDTO envioDTO1 = EnvioDTO.builder()
                .idEnvio(1)
                .ubicacionActual("ubicación 1")
                .destino("destino 1")
                .idEstadoEnvio(1)
                .build();

        List<EnvioDTO> listaDtos = Arrays.asList(envioDTO1);

        // Simular el servicio
        when(envioProductoService.obtenerEnvios()).thenReturn(listaDtos);

        mockMvc.perform(get("/api/envio/obtener-envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verificar los enlaces HATEOAS en la respuesta
                .andExpect(jsonPath("$._links.self.href").exists()) // Verificar que el enlace 'self' existe
                .andExpect(jsonPath("$._embedded.envioDTOList[0]._links.envio.href").exists()) // Verificar que el enlace 'envio' existe
                .andExpect(jsonPath("$._embedded.envioDTOList[0]._links.modificarEstadoEnvio.href").exists()) // Verificar que el enlace 'modificarEstadoEnvio' existe
                .andExpect(jsonPath("$._embedded.envioDTOList[0]._links.eliminarEnvio.href").exists()); // Verificar que el enlace 'eliminarEnvio' existe

        // Verificar que se llamó al servicio una vez
        verify(envioProductoService, times(1)).obtenerEnvios();
    }

    @Test
    public void consultarEnvioByIdTest() throws Exception {

        int idEnvio = 1;
        EnvioDTO envioDTO1 = EnvioDTO.builder()
                .idEnvio(1)
                .ubicacionActual("ubicación 1")
                .destino("destino 1")
                .idEstadoEnvio(1)
                .build();

        when(envioProductoService.consultarEnvioById(anyInt())).thenReturn(Optional.ofNullable(envioDTO1));

        mockMvc.perform(get("/api/envio/{idEnvio}", idEnvio)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEnvio").value(1))
                .andExpect(jsonPath("$._links.envio.href").exists()) // Verificar que el enlace 'envio' existe
                .andExpect(jsonPath("$._links.eliminarEnvio.href").exists()) // Verificar que el enlace 'eliminarEnvio' existe
                .andExpect(jsonPath("$._links.allEnvios.href").exists());

        verify(envioProductoService, times(1)).consultarEnvioById(idEnvio);

    }

    @Test
    public void eliminarEnvioByIdTest() throws Exception {
        int idEnvio = 1;
        EnvioEliminadoDTO envioEliminadoDTO = new EnvioEliminadoDTO("Envio eliminado exitosamente");

        when(envioProductoService.eliminarEnvio(anyInt())).thenReturn(String.valueOf(envioEliminadoDTO));

        mockMvc.perform(delete("/api/envio/eliminar-envio/{id-envio}", idEnvio))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.envio.href").exists()) // Verificar que el enlace 'envio' existe
                .andExpect(jsonPath("$._links.allEnvios.href").exists());

        verify(envioProductoService, times(1)).eliminarEnvio(idEnvio);
    }


}
