package org.monarchinitiative.poet.model.requests;

public class PhenotypeRequest {

    private Long id;
    private String hpoName;
    private String hpoId;
    private String evidenceType;
    private String comment;
    private String ageOfOnset;
    private String aspect;
    private String description;
    private String publicationId;
    private String publicationName;
    private String diseaseId;
    private String diseaseName;

    public PhenotypeRequest(Long id, String hpoName, String hpoId, String evidenceType, String comment,
                            String ageOfOnset, String aspect, String description, String publicationId,
                            String publicationName, String diseaseId, String diseaseName) {
        this.id = id;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.evidenceType = evidenceType;
        this.comment = comment;
        this.ageOfOnset = ageOfOnset;
        this.aspect = aspect;
        this.description = description;
        this.publicationId = publicationId;
        this.publicationName = publicationName;
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
    }

    public Long getId() {
        return id;
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

    public String getAgeOfOnset() {
        return ageOfOnset;
    }

    public String getAspect() {
        return aspect;
    }

    public String getDescription() {
        return description;
    }

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
}
