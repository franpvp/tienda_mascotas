package com.duoc.tiendamascotas.controllers;

import com.duoc.tiendamascotas.dto.DetalleEnvioProductoDTO;
import com.duoc.tiendamascotas.dto.EnvioDTO;
import com.duoc.tiendamascotas.dto.EnvioEliminadoDTO;
import com.duoc.tiendamascotas.dto.UbicacionActualDTO;
import com.duoc.tiendamascotas.entities.EnvioEntity;
import com.duoc.tiendamascotas.exceptions.EnvioNotFoundException;
import com.duoc.tiendamascotas.services.EnvioProductoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/envio")
public class EnvioController {

    @Autowired
    private EnvioProductoService envioProductoService;

    @PostMapping
    public ResponseEntity<EntityModel<EnvioDTO>> generarEnvio(
            @Valid @RequestBody EnvioDTO envioDTO
    ) {
        // Lógica para generar el envío
        EnvioDTO envioGenerado = envioProductoService.generarEnvio(envioDTO);

        EntityModel<EnvioDTO> envioModel = EntityModel.of(envioGenerado,
                linkTo(methodOn(EnvioController.class).obtenerEnvioById(envioGenerado.getIdEnvio())).withSelfRel(),
                linkTo(methodOn(EnvioController.class).modificarEstadoEnvio(envioGenerado.getIdEnvio(), envioGenerado.getIdEstadoEnvio())).withRel("modificarEstadoEnvio"),
                linkTo(methodOn(EnvioController.class).obtenerEnvios()).withRel("allEnvios"));

        return new ResponseEntity<>(envioModel, HttpStatus.CREATED);

    }

    @PutMapping("/modificar-estado/{id-envio}/{id-estado}")
    public ResponseEntity<EntityModel<EnvioDTO>> modificarEstadoEnvio(
            @PathVariable("id-envio") int idEnvio,
            @PathVariable("id-estado") int idEstadoEnvio) {

        // Modificar el estado del envío y devolver el DTO modificado
        Optional<EnvioDTO> envioModificadoOpt = envioProductoService.modificarEstadoEnvio(idEnvio, idEstadoEnvio);

        // Verificar si el envío fue encontrado
        if (envioModificadoOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no se encontró, devolver un 404
        }
        EnvioDTO envioModificado = envioModificadoOpt.get();

        // Crear EntityModel con los enlaces
        EntityModel<EnvioDTO> responseModel = EntityModel.of(envioModificado,
                linkTo(methodOn(EnvioController.class).obtenerEnvioById(envioModificado.getIdEnvio())).withSelfRel(),
                linkTo(methodOn(EnvioController.class).eliminarEnvioById(idEnvio)).withRel("eliminarEnvio"),
                linkTo(methodOn(EnvioController.class).obtenerEnvios()).withRel("allEnvios"));
        // Devolver la respuesta con el EntityModel y el status OK
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @GetMapping("/obtener-envios")
    public ResponseEntity<CollectionModel<EntityModel<EnvioDTO>>> obtenerEnvios() {
        List<EnvioDTO> envios = envioProductoService.obtenerEnvios();

        List<EntityModel<EnvioDTO>> envioModels = envios.stream()
                .map(envio -> EntityModel.of(envio,
                        linkTo(methodOn(EnvioController.class).obtenerEnvioById(envio.getIdEnvio())).withRel("envio"),
                        linkTo(methodOn(EnvioController.class).modificarEstadoEnvio(envio.getIdEnvio(), envio.getIdEstadoEnvio())).withRel("modificarEstadoEnvio"),
                        linkTo(methodOn(EnvioController.class).eliminarEnvioById(envio.getIdEnvio())).withRel("eliminarEnvio")
                ))
                .collect(Collectors.toList());

        // Crear CollectionModel con la lista de EntityModels y el enlace a todos los envíos
        CollectionModel<EntityModel<EnvioDTO>> collectionModel = CollectionModel.of(envioModels,
                linkTo(methodOn(EnvioController.class).obtenerEnvios()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping("/{id-envio}")
    public ResponseEntity<EntityModel<Optional<EnvioDTO>>> obtenerEnvioById(@PathVariable("id-envio") int idEnvio) {
        // Lógica para obtener un envío por su ID
        Optional<EnvioDTO> envio = envioProductoService.consultarEnvioById(idEnvio);

        EntityModel<Optional<EnvioDTO>> envioModel = EntityModel.of(envio,
                linkTo(methodOn(EnvioController.class).obtenerEnvioById(idEnvio)).withRel("envio"),
                linkTo(methodOn(EnvioController.class).modificarEstadoEnvio(idEnvio, envio.get().getIdEstadoEnvio())).withRel("modificarEstadoEnvio"),
                linkTo(methodOn(EnvioController.class).eliminarEnvioById(idEnvio)).withRel("eliminarEnvio"),
                linkTo(methodOn(EnvioController.class).obtenerEnvios()).withRel("allEnvios"));

        return new ResponseEntity<>(envioModel, HttpStatus.OK);
    }

    @GetMapping("/ubicacion/{id-envio}")
    public ResponseEntity<EntityModel<UbicacionActualDTO>> getUbicacionActual(@PathVariable("id-envio") int idEnvio) {

        EnvioDTO envio = envioProductoService.consultarEnvioById(idEnvio)
                .orElseThrow(() -> new EnvioNotFoundException("Envio no encontrado para el id: " + idEnvio));

        UbicacionActualDTO ubicacionActualDTO = new UbicacionActualDTO(envio.getUbicacionActual());

        WebMvcLinkBuilder linkToEnvio = linkTo(methodOn(EnvioController.class).obtenerEnvioById(idEnvio));
        WebMvcLinkBuilder linkToEliminarEnvio = linkTo(methodOn(EnvioController.class).eliminarEnvioById(idEnvio));
        WebMvcLinkBuilder linkToAllEnvios = linkTo(methodOn(EnvioController.class).obtenerEnvios());

        EntityModel<UbicacionActualDTO> ubicacionModel = EntityModel.of(ubicacionActualDTO,
                linkToEnvio.withRel("envio"),
                linkToEliminarEnvio.withRel("eliminarEnvio"),
                linkToAllEnvios.withRel("allEnvios"));

        return new ResponseEntity<>(ubicacionModel, HttpStatus.OK);
    }


    @DeleteMapping("/eliminar-envio/{id-envio}")
    public ResponseEntity<EntityModel<EnvioEliminadoDTO>> eliminarEnvioById(@PathVariable("id-envio") int idEnvio) {

        envioProductoService.eliminarEnvio(idEnvio);
        EnvioEliminadoDTO envioEliminadoDTO = new EnvioEliminadoDTO("Envio eliminado con id: " + idEnvio);

        WebMvcLinkBuilder linkToEnvio = linkTo(methodOn(EnvioController.class).obtenerEnvioById(idEnvio));
        WebMvcLinkBuilder linkToAllEnvios = linkTo(methodOn(EnvioController.class).obtenerEnvios());

        EntityModel<EnvioEliminadoDTO> responseModel = EntityModel.of(envioEliminadoDTO,
                linkToEnvio.withRel("envio"),
                linkToAllEnvios.withRel("allEnvios"));

        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }


}
