package org.monarchinitiative.poet.model.response;

public class Contribution {
    Integer maxo;
    Integer hpo;
    Integer phenopackets;

    public Contribution(Integer maxo, Integer hpo, Integer phenopackets) {
        this.maxo = maxo == null ? 0 : maxo;
        this.hpo = hpo == null ? 0 : hpo;
        this.phenopackets = phenopackets == null ? 0 : phenopackets;
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
