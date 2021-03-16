package org.monarchinitiative.poet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class AnnotationSourceException extends RuntimeException {


    public AnnotationSourceException(String diseaseId) {
        super(String.format("Could not find a valid disease with id %s ", diseaseId));
    }

    public AnnotationSourceException(String publicationId, String diseaseId) {
        super(String.format("Could not find a valid disease with id %s or publication with id %s.", diseaseId, publicationId));
    }
}
