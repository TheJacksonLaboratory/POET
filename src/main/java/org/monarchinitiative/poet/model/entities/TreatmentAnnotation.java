package org.monarchinitiative.poet.model.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.requests.TreatmentRequest;
import org.monarchinitiative.poet.views.AnnotationViews;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("treatment")
public class TreatmentAnnotation extends Annotation {

    @JsonView(AnnotationViews.Simple.class)
    private String maxoId;
    @JsonView(AnnotationViews.Simple.class)
    private String maxoName;
    @JsonView(AnnotationViews.Simple.class)
    private String hpoId;
    @JsonView(AnnotationViews.Simple.class)
    private String hpoName;
    @JsonView(AnnotationViews.Simple.class)
    private String evidenceType;
    @JsonView(AnnotationViews.Simple.class)
    private String relation;
    @JsonView(AnnotationViews.Simple.class)
    private String extensionId;
    @JsonView(AnnotationViews.Simple.class)
    private String extensionLabel;
    @JsonView(AnnotationViews.Simple.class)
    private String comment;


    public TreatmentAnnotation(){}

    public TreatmentAnnotation(AnnotationSource annotationSource, AnnotationStatus status, String maxoId,
                               String maxoName, String hpoName, String hpoId, String evidenceType, String comment, String relation,
                               String extensionId, String extensionLabel) {
        super(annotationSource, status);
        this.maxoId = maxoId;
        this.maxoName = maxoName;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.evidenceType = evidenceType;
        this.comment = comment;
        this.relation = relation;
        this.extensionId = extensionId;
        this.extensionLabel = extensionLabel;
    }

    public TreatmentAnnotation(String maxoId, String maxoName, String hpoName, String hpoId, String evidenceType,
                               String comment, String relation, String extensionId, String extensionLabel) {
        this.maxoId = maxoId;
        this.maxoName = maxoName;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.evidenceType = evidenceType;
        this.comment = comment;
        this.relation = relation;
        this.extensionId = extensionId;
        this.extensionLabel = extensionLabel;
    }

    public TreatmentAnnotation(TreatmentRequest treatmentRequest, AnnotationSource annotationSource){
        super(annotationSource, AnnotationStatus.UNDER_REVIEW);
        this.maxoId = treatmentRequest.getMaxoId();
        this.maxoName = treatmentRequest.getMaxoName();
        this.hpoId = treatmentRequest.getHpoId();
        this.hpoName = treatmentRequest.getHpoName();
        this.evidenceType = treatmentRequest.getEvidence();
        this.comment = treatmentRequest.getComment();
        this.relation = treatmentRequest.getRelation();
        this.extensionId = treatmentRequest.getExtensionId();
        this.extensionLabel = treatmentRequest.getExtensionLabel();
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

    public String getExtensionId() {
        return extensionId;
    }

    public String getExtensionLabel() {
        return extensionLabel;
    }
}
