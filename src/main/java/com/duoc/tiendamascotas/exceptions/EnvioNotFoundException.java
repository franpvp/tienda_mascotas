package com.duoc.tiendamascotas.exceptions;

public class EnvioNotFoundException extends RuntimeException{
    public EnvioNotFoundException(String message) {
        super(message);
    }
}