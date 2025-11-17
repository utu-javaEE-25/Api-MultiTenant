// RUTA: src/main/java/uy/tse/periferico/exception/ResourceAlreadyExistsException.java
package uy.tse.periferico.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT) // Devuelve un código 409
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}