package org.monarchinitiative.poet.model.entities;


import javax.persistence.*;

@Entity
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private AnnotationSource annotationSource;
}

