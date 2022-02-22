package org.monarchinitiative.poet.model.requests;

import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;

import java.util.Objects;

/**
 * The model for the JSON requests for publication endpoints with validation
 */
public class PublicationRequest {

    private Publication publication;
    private Disease disease;

    public PublicationRequest(Publication publication, Disease disease) {
        this.publication = publication;
        this.disease = disease;
    }

    public Publication getPublication() {
        return publication;
    }

    public Disease getDisease() {
        return disease;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicationRequest that = (PublicationRequest) o;
        return Objects.equals(publication, that.publication) &&
                Objects.equals(disease, that.disease);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publication, disease);
    }
}
