package org.monarchinitiative.poet.model;

public class MaxoRequest {


    private String maxoId;
    private String maxoName;
    private String hpoName;
    private String hpoId;
    private String evidence;
    private String comment;
    private String relation;
    private String extension;
    private String publicationId;
    private String publicationName;
    private String diseaseId;
    private String diseaseName;


    MaxoRequest(){}

    public MaxoRequest(String maxoId, String maxoName, String hpoName, String hpoId, String evidence, String comment, String relation, String extension, String publicationId, String publicationName, String diseaseId, String diseaseName) {
        this.maxoId = maxoId;
        this.maxoName = maxoName;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.evidence = evidence;
        this.comment = comment;
        this.relation = relation;
        this.extension = extension;
        this.publicationId = publicationId;
        this.publicationName = publicationName;
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
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

    public String getExtension() {
        return extension;
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
