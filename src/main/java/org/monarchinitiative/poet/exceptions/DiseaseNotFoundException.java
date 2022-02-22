package org.monarchinitiative.poet.exceptions;

public class DiseaseNotFoundException extends RuntimeException {
    public DiseaseNotFoundException(String id) {
        super("Could not find disease: " + id);
    }
}
