package org.monarchinitiative.poet.model.entities;

import javax.persistence.*;

@Entity
public class AnnotationSource {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Publication publication;

    @ManyToOne
    private Disease disease;

    public AnnotationSource(){}

    public AnnotationSource(Publication publication, Disease disease){
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
