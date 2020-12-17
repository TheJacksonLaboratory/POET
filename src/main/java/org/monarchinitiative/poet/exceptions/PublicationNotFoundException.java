package org.monarchinitiative.poet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class PublicationNotFoundException extends RuntimeException {
    PublicationNotFoundException(String id) {
        super("Could not find publication: " + id);
    }
}
