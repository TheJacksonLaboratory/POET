package org.monarchinitiative.poet.model.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String diseaseId;
    private String diseaseName;
    private String alternateId;

    @OneToMany(mappedBy = "disease")
    private List<AnnotationSource> amnotationSource;

    public String getDiseaseId() {
        return diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getAlternateId() {
        return alternateId;
    }
}
