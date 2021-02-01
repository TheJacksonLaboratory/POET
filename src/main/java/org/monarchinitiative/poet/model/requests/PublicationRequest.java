package org.monarchinitiative.poet.model.requests;

import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;

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
}
