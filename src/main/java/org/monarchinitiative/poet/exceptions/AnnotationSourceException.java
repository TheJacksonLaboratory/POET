package org.monarchinitiative.poet.exceptions;

public class AnnotationSourceException extends RuntimeException {


    public AnnotationSourceException(String message) {
        super(message);
    }

    public static AnnotationSourceException exists(String publicationId, String diseaseId){
        return new AnnotationSourceException(String.format("Annotation source exists for disease %s and publication %s.", diseaseId, publicationId));
    }

    public static AnnotationSourceException diseaseNotFound(String diseaseId){
        return new AnnotationSourceException(String.format("Could not find a valid disease with id %s.", diseaseId));
    }

    public static AnnotationSourceException bothNotFound(String publicationId, String diseaseId){
        return new AnnotationSourceException(String.format("Could not find a valid disease with id %s or publication with id %s.", diseaseId, publicationId));
    }


}
