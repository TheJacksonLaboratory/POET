package org.monarchinitiative.poet.model.entities;


import org.monarchinitiative.poet.model.AnnotationStatus;
import org.monarchinitiative.poet.model.MaxoRequest;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("maxo")
public class MaxoAnnotation extends Annotation {

    private String maxoId;
    private String maxoName;
    private String hpoName;
    private String hpoId;
    private String eco;
    private String comment;
    private String relation;
    private String extension;

    public MaxoAnnotation(){}

    public MaxoAnnotation(AnnotationSource annotationSource, AnnotationStatus status, String maxoId,
                          String maxoName, String hpoName, String hpoId, String eco, String comment, String relation,
                          String extension) {
        super(annotationSource, status);
        this.maxoId = maxoId;
        this.maxoName = maxoName;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.eco = eco;
        this.comment = comment;
        this.relation = relation;
        this.extension = extension;
    }

    public MaxoAnnotation(String maxoId, String maxoName, String hpoName, String hpoId, String eco, String comment, String relation, String extension) {
        this.maxoId = maxoId;
        this.maxoName = maxoName;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.eco = eco;
        this.comment = comment;
        this.relation = relation;
        this.extension = extension;
    }

    public MaxoAnnotation(MaxoRequest maxoRequest, AnnotationSource annotationSource){
        super(annotationSource, AnnotationStatus.UNDER_REVIEW);
        this.maxoId = maxoRequest.getMaxoId();
        this.maxoName = maxoRequest.getMaxoName();
        this.hpoId = maxoRequest.getHpoId();
        this.hpoName = maxoRequest.getHpoName();
        this.eco = maxoRequest.getEco();
        this.comment = maxoRequest.getComment();
        this.relation = maxoRequest.getRelation();
        this.extension = maxoRequest.getExtension();
    }

    public String getMaxoId() {
        return maxoId;
    }

    public String getMaxoName() {
        return maxoName;
    }

    public String getHpoName() {
        return hpoName;
    }

    public String getHpoId() {
        return hpoId;
    }

    public String getEco() {
        return eco;
    }

    public String getComment() {
        return comment;
    }

    public String getRelation() {
        return relation;
    }

    public String getExtension() {
        return extension;
    }
}
