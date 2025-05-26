package com.torneos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Puedes cambiar el status según el caso
public class TorneosException extends RuntimeException {

    public TorneosException(String mensaje) {
        super(mensaje);
    }

    public TorneosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}