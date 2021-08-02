package org.monarchinitiative.poet.model.requests;

public class DiseaseRequest {
    private final String id;
    private final String diseaseId;
    private final String diseaseName;
    private final String description;
    private final String equivalentId;

    public DiseaseRequest(String id, String diseaseId, String diseaseName, String description, String equivalentId) {
        this.id = id;
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.description = description;
        this.equivalentId = equivalentId;
    }

    public String getId() {
        return id;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getDescription() {
        return description;
    }

    public String getEquivalentId() {
        return equivalentId;
    }
}
