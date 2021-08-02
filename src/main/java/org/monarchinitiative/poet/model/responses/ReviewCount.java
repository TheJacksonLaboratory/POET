package org.monarchinitiative.poet.model.responses;

import org.monarchinitiative.poet.model.entities.Disease;

public class ReviewCount {
    Disease disease;
    long count;
    String type;

    public ReviewCount(Disease disease, long count, String type) {
        this.disease = disease;
        this.count = count;
        this.type = type;
    }

    public String getDiseaseName() {
        return disease.getDiseaseName();
    }

    public String getDiseaseId() {
        return disease.getDiseaseId();
    }

    public Long getCount(){
        return count;
    }

    public String getType() {
        return type;
    }
}
