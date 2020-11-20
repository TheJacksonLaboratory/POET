package org.monarchinitiative.poet.model;

public class MaxoRequest {


    private String maxoId;
    private String maxoName;
    private String hpoName;
    private String hpoId;
    private String eco;
    private String comment;
    private String relation;
    private String extension;
    private String publicationIdentifier;
    private String publicationName;
    private String diseaseId;
    private String diseaseName;


    MaxoRequest(){}

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

    public String getPublicationIdentifier() {
        return publicationIdentifier;
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
