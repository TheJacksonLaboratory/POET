package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.views.AnnotationViews;

import javax.persistence.*;

@Entity
public class AnnotationSource {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JsonView(AnnotationViews.Simple.class)
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
