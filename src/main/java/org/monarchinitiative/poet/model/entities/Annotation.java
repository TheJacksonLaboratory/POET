package org.monarchinitiative.poet.model.entities;


import org.monarchinitiative.poet.model.AnnotationStatus;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="annotation_type",
        discriminatorType = DiscriminatorType.STRING)
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private AnnotationSource annotationSource;

    @Enumerated(EnumType.STRING)
    private AnnotationStatus status;

    public Annotation(){}

    public Annotation(AnnotationSource annotationSource, AnnotationStatus status) {
        this.annotationSource = annotationSource;
        this.status = status;
    }

    public AnnotationSource getAnnotationSource() {
        return annotationSource;
    }

    public void setAnnotationSource(AnnotationSource annotationSource) {
        this.annotationSource = annotationSource;
    }

    public AnnotationStatus getStatus() {
        return status;
    }

    public void setStatus(AnnotationStatus status) {
        this.status = status;
    }
}

