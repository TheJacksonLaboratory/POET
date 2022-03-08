package org.monarchinitiative.poet.model.responses;

public class DiseaseCount {
    final String diseaseId;
    final String diseaseName;
    final Long activityCount;

    public DiseaseCount(String diseaseId, String diseaseName, Long activityCount) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.activityCount = activityCount;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public Long getDiseaseCount() {
        return activityCount;
    }
}
