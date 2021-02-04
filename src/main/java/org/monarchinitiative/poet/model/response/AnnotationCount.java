package org.monarchinitiative.poet.model.response;

public class AnnotationCount {

    private final Integer phenotypeCount;
    private final Integer treatmentCount;

    public AnnotationCount(Integer phenotypeCount, Integer treatmentCount) {
        this.phenotypeCount = phenotypeCount;
        this.treatmentCount = treatmentCount;
    }

    public Integer getPhenotypeCount() {
        return phenotypeCount;
    }

    public Integer getTreatmentCount() {
        return treatmentCount;
    }
}
