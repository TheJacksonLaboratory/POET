package org.monarchinitiative.poet.model.responses;

public class DiseaseCount {
    final String diseaseId;
    final String diseaseName;
    final long activityCount;

    public DiseaseCount(String diseaseId, String diseaseName, long activityCount) {
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

    public long getDiseaseCount() {
        return activityCount;
    }
}
