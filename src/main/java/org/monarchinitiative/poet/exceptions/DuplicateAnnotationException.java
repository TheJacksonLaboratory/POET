package org.monarchinitiative.poet.exceptions;

public class DuplicateAnnotationException extends RuntimeException  {
    public DuplicateAnnotationException(String annotationType, String diseaseName) {
        super(String.format("Duplicate %s annotation already exists for %s.", annotationType, diseaseName));
    }
}
