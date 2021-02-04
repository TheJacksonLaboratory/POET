package org.monarchinitiative.poet.model.entities;


import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.requests.TreatmentRequest;
import org.monarchinitiative.poet.views.AnnotationViews;

import javax.persistence.*;
import java.util.Objects;

@Table(uniqueConstraints=
        @UniqueConstraint(columnNames={"maxoId", "hpoId", "evidenceType", "relation", "extensionId"})
)
@Entity()
@DiscriminatorValue("maxo")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TreatmentAnnotation that = (TreatmentAnnotation) o;
        return Objects.equals(maxoId, that.maxoId) &&
                Objects.equals(maxoName, that.maxoName) &&
                Objects.equals(hpoId, that.hpoId) &&
                Objects.equals(hpoName, that.hpoName) &&
                Objects.equals(evidenceType, that.evidenceType) &&
                Objects.equals(relation, that.relation) &&
                Objects.equals(extensionId, that.extensionId) &&
                Objects.equals(extensionLabel, that.extensionLabel) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), maxoId, maxoName, hpoName, hpoId, evidenceType, comment, relation, extensionId, extensionLabel);
    }
}
