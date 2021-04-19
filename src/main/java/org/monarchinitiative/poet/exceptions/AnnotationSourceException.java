package org.monarchinitiative.poet.exceptions;

public class AnnotationSourceException extends RuntimeException {


    public AnnotationSourceException(String diseaseId) {
        super(String.format("Could not find a valid disease with id %s.", diseaseId));
    }

    public AnnotationSourceException(String publicationId, String diseaseId) {
        super(String.format("Could not find a valid disease with id %s or publication with id %s.", diseaseId, publicationId));
    }
}
