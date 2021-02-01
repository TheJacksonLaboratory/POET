package org.monarchinitiative.poet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class DiseaseNotFoundException extends RuntimeException {
    public DiseaseNotFoundException(String id) {
        super("Could not find disease: " + id);
    }
}
