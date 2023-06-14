package org.monarchinitiative.poet.model.responses;

import org.monarchinitiative.poet.model.enumeration.Category;

 public class ReviewCount {
    private final String diseaseId;
    private final String diseaseName;
    private final long count;
    private Category type;

    public ReviewCount(String diseaseId, String diseaseName, long count, String type) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.count = count;
        try {
            this.type = Category.valueOf(type.toUpperCase());
        } catch (Exception e) {
            this.type = Category.UNKNOWN;
        }

    }

    public ReviewCount(String diseaseId, String diseaseName, long count, Category type) {
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
        return type.toString();
    }
}
