package org.monarchinitiative.poet.model.entities;


import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.enumeration.Relation;
import org.monarchinitiative.poet.model.requests.TreatmentRequest;
import org.monarchinitiative.poet.views.AnnotationViews;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("treatment")
public class TreatmentAnnotation extends Annotation {

    @JsonView(AnnotationViews.Simple.class)
    @NotNull
    private String maxoId;
    @JsonView(AnnotationViews.Simple.class)
    @NotNull
    private String maxoName;
    @JsonView(AnnotationViews.Simple.class)
    @NotNull
    private String hpoId;
    @JsonView(AnnotationViews.Simple.class)
    @NotNull
    private String hpoName;
    @JsonView(AnnotationViews.Simple.class)
    @NotNull
    private String evidence;
    @JsonView(AnnotationViews.Simple.class)
    @NotNull
    @Enumerated
    private Relation relation;
    @JsonView(AnnotationViews.Simple.class)
    private String extensionId;
    @JsonView(AnnotationViews.Simple.class)
    private String extensionLabel;
    @JsonView(AnnotationViews.Simple.class)
    private String comment;


    public TreatmentAnnotation(){}

    public TreatmentAnnotation(AnnotationSource annotationSource, AnnotationStatus status, User owner, String maxoId,
                               String maxoName, String hpoName, String hpoId, String evidence, String comment, String relation,
                               String extensionId, String extensionLabel) {
        super(annotationSource, status, owner);
        this.maxoId = maxoId;
        this.maxoName = maxoName;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.evidence = evidence;
        this.comment = comment;
        this.relation = Relation.valueOf(relation);
        this.extensionId = extensionId;
        this.extensionLabel = extensionLabel;
    }

    public TreatmentAnnotation(String maxoId, String maxoName, String hpoName, String hpoId, String evidence,
                               String comment, String relation, String extensionId, String extensionLabel) {
        this.maxoId = maxoId;
        this.maxoName = maxoName;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.evidence = evidence;
        this.comment = comment;
        this.relation = Relation.valueOf(relation);
        this.extensionId = extensionId;
        this.extensionLabel = extensionLabel;
    }

    public TreatmentAnnotation(TreatmentRequest treatmentRequest, AnnotationSource annotationSource,
                              AnnotationStatus status, User user){

        super(annotationSource, status, user);
        this.maxoId = treatmentRequest.getMaxoId();
        this.maxoName = treatmentRequest.getMaxoName();
        this.hpoId = treatmentRequest.getHpoId();
        this.hpoName = treatmentRequest.getHpoName();
        this.evidence = treatmentRequest.getEvidence();
        this.comment = treatmentRequest.getComment();
        this.relation = Relation.valueOf(treatmentRequest.getRelation());
        this.extensionId = treatmentRequest.getExtensionId();
        this.extensionLabel = treatmentRequest.getExtensionLabel();
    }

    public void updateAnnotation(TreatmentRequest treatmentRequest, AnnotationSource annotationSource){
        this.maxoId = treatmentRequest.getMaxoId();
        this.maxoName = treatmentRequest.getMaxoName();
        this.hpoId = treatmentRequest.getHpoId();
        this.hpoName = treatmentRequest.getHpoName();
        this.evidence = treatmentRequest.getEvidence();
        this.comment = treatmentRequest.getComment();
        this.relation = Relation.valueOf(treatmentRequest.getRelation());
        this.extensionId = treatmentRequest.getExtensionId();
        this.extensionLabel = treatmentRequest.getExtensionLabel();
        this.setAnnotationSource(annotationSource);
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

    public String getEvidence() {
        return evidence;
    }

    public String getComment() {
        return comment;
    }

    public String getRelation() {
        return relation.toString();
    }

    public String getExtensionId() {
        return extensionId;
    }

    public String getExtensionLabel() {
        return extensionLabel;
    }
}
