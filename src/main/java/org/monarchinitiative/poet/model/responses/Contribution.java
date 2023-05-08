package org.monarchinitiative.poet.model.responses;

public class Contribution {
    final int treatment;
    final int phenotype;

    public Contribution(int treatment, int phenotype) {
        this.treatment = Math.max(treatment, 0);
        this.phenotype = Math.max(phenotype, 0);
    }

    public int getTreatment() {
        return treatment;
    }

    public int getPhenotype() {
        return phenotype;
    }

}
