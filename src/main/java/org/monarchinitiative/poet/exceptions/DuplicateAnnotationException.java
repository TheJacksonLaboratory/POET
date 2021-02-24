package org.monarchinitiative.poet.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class DuplicateAnnotationException extends RuntimeException  {
    public DuplicateAnnotationException(String annotationType, String diseaseName) {
        super(String.format("Duplicate %s annotation already exists for %s.", annotationType, diseaseName));
    }
}
