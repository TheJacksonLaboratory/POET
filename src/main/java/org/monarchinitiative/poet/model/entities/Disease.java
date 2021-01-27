package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.views.AnnotationViews;
import org.monarchinitiative.poet.views.DiseaseViews;
import org.monarchinitiative.poet.views.PublicationViews;
import org.monarchinitiative.poet.views.UserActivityViews;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView({DiseaseViews.Simple.class, AnnotationViews.Simple.class, UserActivityViews.Simple.class})
    @Column(nullable = false)
    private String diseaseId;

    @JsonView({DiseaseViews.Simple.class, AnnotationViews.Simple.class, UserActivityViews.Simple.class})
    private String diseaseName;

    @OneToMany(mappedBy = "disease")
    @JsonView(PublicationViews.Simple.class)
    private List<AnnotationSource> annotationSource;

    protected Disease(){}

    public Disease(String diseaseId, String diseaseName) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
    }

    public Disease(String diseaseId, String diseaseName, List<AnnotationSource> annotationSources) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.annotationSource = annotationSources;
    }


    public String getDiseaseId() {
        return diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public List<Publication> getPublications(){
        return this.annotationSource.stream().map(AnnotationSource::getPublication).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease disease = (Disease) o;
        return Objects.equals(id, disease.id) &&
                Objects.equals(diseaseId, disease.diseaseId) &&
                Objects.equals(diseaseName, disease.diseaseName) &&
                Objects.equals(annotationSource, disease.annotationSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, diseaseId, diseaseName, annotationSource);
    }
}
