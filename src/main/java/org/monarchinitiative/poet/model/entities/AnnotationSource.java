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
}
