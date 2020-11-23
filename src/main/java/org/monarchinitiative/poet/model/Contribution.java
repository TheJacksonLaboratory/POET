package org.monarchinitiative.poet.model;

public class Contribution {
    Integer maxo;
    Integer hpo;
    Integer phenopackets;

    public Contribution(Integer maxo, Integer hpo, Integer phenopackets) {
        this.maxo = maxo;
        this.hpo = hpo;
        this.phenopackets = phenopackets;
    }

    public Integer getMaxo() {
        return maxo;
    }

    public Integer getHpo() {
        return hpo;
    }

    public Integer getPhenopackets() {
        return phenopackets;
    }
}
