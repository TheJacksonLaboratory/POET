package org.monarchinitiative.poet.model.responses;

import org.monarchinitiative.poet.model.entities.Disease;

public class ReviewCount {
    final String diseaseId;
    final String diseaseName;
    final long count;
    final String type;

    public ReviewCount(String diseaseId, String diseaseName, long count, String type) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.count = count;
        this.type = type;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public Long getCount(){
        return count;
    }

    public String getType() {
        return type;
    }
}
