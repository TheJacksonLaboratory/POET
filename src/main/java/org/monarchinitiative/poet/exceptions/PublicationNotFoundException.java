package org.monarchinitiative.poet.exceptions;

public class PublicationNotFoundException extends RuntimeException {
    PublicationNotFoundException(String id) {
        super("Could not find publication: " + id);
    }
}
