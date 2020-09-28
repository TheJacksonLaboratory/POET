package org.monarchinitiative.poet.model.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String identifier;
    private String name;

    @OneToMany(mappedBy = "disease")
    private List<AnnotationSource> amnotationSource;

    protected Disease(){}

    public Disease(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public String getDiseaseId() {
        return identifier;
    }

    public String getDiseaseName() {
        return name;
    }
}
