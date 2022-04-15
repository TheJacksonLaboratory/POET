package org.monarchinitiative.poet.exceptions;

public class ExportException extends RuntimeException {

    public ExportException(String ontology) {
        super(String.format("Could not generate export file for %s", ontology));
    }
}
