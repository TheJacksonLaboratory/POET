package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.views.DiseaseViews;
import org.monarchinitiative.poet.views.PublicationViews;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(DiseaseViews.Simple.class)
    @Column(nullable = false)
    private String diseaseId;

    @JsonView(DiseaseViews.Simple.class)
    private String diseaseName;

    @OneToMany(mappedBy = "disease")
    @JsonView(PublicationViews.Simple.class)
    private List<AnnotationSource> annotationSource;

    protected Disease(){}

    public Disease(String diseaseId, String diseaseName) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
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
}
