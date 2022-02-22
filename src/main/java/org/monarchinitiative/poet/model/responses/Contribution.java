package org.monarchinitiative.poet.model.responses;

public class Contribution {
    Integer treatment;
    Integer phenotype;
    Integer phenopackets;

    public Contribution(Integer treatment, Integer phenotype, Integer phenopackets) {
        this.treatment = treatment == null ? 0 : treatment;
        this.phenotype = phenotype == null ? 0 : phenotype;
        this.phenopackets = phenopackets == null ? 0 : phenopackets;
    }

    public Integer getTreatment() {
        return treatment;
    }

    public Integer getPhenotype() {
        return phenotype;
    }

    public Integer getPhenopackets() {
        return phenopackets;
    }
}
