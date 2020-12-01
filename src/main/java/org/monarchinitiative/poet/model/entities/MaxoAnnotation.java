package org.monarchinitiative.poet.model.entities;


import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.MaxoRequest;
import org.monarchinitiative.poet.views.AnnotationViews;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("maxo")
public class MaxoAnnotation extends Annotation {

    @JsonView(AnnotationViews.Simple.class)
    private String maxoId;
    @JsonView(AnnotationViews.Simple.class)
    private String maxoName;
    @JsonView(AnnotationViews.Simple.class)
    private String hpoName;
    @JsonView(AnnotationViews.Simple.class)
    private String hpoId;
    @JsonView(AnnotationViews.Simple.class)
    private String evidenceType;
    @JsonView(AnnotationViews.Simple.class)
    private String comment;
    @JsonView(AnnotationViews.Simple.class)
    private String relation;
    @JsonView(AnnotationViews.Simple.class)
    private String extension;

    public MaxoAnnotation(){}

    public MaxoAnnotation(AnnotationSource annotationSource, AnnotationStatus status, String maxoId,
                          String maxoName, String hpoName, String hpoId, String evidenceType, String comment, String relation,
                          String extension) {
        super(annotationSource, status);
        this.maxoId = maxoId;
        this.maxoName = maxoName;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.evidenceType = evidenceType;
        this.comment = comment;
        this.relation = relation;
        this.extension = extension;
    }

    public MaxoAnnotation(String maxoId, String maxoName, String hpoName, String hpoId, String evidenceType,
                          String comment, String relation, String extension) {
        this.maxoId = maxoId;
        this.maxoName = maxoName;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.evidenceType = evidenceType;
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
        this.evidenceType = maxoRequest.getEvidenceType();
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

    public String getEvidenceType() {
        return evidenceType;
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
