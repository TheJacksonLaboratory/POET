package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.views.AnnotationViews;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationSource that = (AnnotationSource) o;
        return id == that.id &&
                Objects.equals(publication, that.publication) &&
                Objects.equals(disease, that.disease);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publication, disease);
    }
}
