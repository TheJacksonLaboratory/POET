package org.monarchinitiative.poet.exceptions;

public class AnnotationSourceException extends RuntimeException {
    public AnnotationSourceException(String publicationId, String diseaseId) {
        super(String.format("Could not find a valid annotation source with publication id %s and disease id %s", publicationId, diseaseId));
    }
}
