package org.monarchinitiative.poet.model.requests;

import java.util.Objects;

public class TreatmentRequest {


    private Long id;
    private String maxoId;
    private String maxoName;
    private String hpoName;
    private String hpoId;
    private String evidence;
    private String comment;
    private String relation;
    private String extensionId;
    private String extensionLabel;
    private String publicationId;
    private String publicationName;
    private String diseaseId;
    private String diseaseName;


    TreatmentRequest(){}

    public TreatmentRequest(Long id, String maxoId, String maxoName, String hpoName, String hpoId, String evidence,
                            String comment, String relation, String extensionId, String extensionLabel,
                            String publicationId, String publicationName, String diseaseId, String diseaseName) {
        this.id = id;
        this.maxoId = maxoId;
        this.maxoName = maxoName;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.evidence = evidence;
        this.comment = comment;
        this.relation = relation;
        this.extensionId = extensionId;
        this.extensionLabel = extensionLabel;
        this.publicationId = publicationId;
        this.publicationName = publicationName;
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
    }

    public Long getId() {
        return id;
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
        return relation;
    }

    public String getExtensionId() {
        return extensionId;
    }

    public String getExtensionLabel() { return extensionLabel;}

    public String getPublicationId() {
        return publicationId;
    }

    public String getPublicationName() {
        return publicationName;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreatmentRequest that = (TreatmentRequest) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(maxoId, that.maxoId) &&
                Objects.equals(maxoName, that.maxoName) &&
                Objects.equals(hpoName, that.hpoName) &&
                Objects.equals(hpoId, that.hpoId) &&
                Objects.equals(evidence, that.evidence) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(relation, that.relation) &&
                Objects.equals(extensionId, that.extensionId) &&
                Objects.equals(publicationId, that.publicationId) &&
                Objects.equals(publicationName, that.publicationName) &&
                Objects.equals(diseaseId, that.diseaseId) &&
                Objects.equals(diseaseName, that.diseaseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, maxoId, maxoName, hpoName, hpoId, evidence, comment, relation, extensionId, publicationId, publicationName, diseaseId, diseaseName);
    }


}
