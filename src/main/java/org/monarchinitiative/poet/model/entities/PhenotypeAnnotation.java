package org.monarchinitiative.poet.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class PhenotypeAnnotation {
    private @Id @GeneratedValue Long id;
    private String diseaseId;
    private String diseaseName;
    private String hpoId;
    private String hpoName;
    private String ageOfOnset;
    private String evidence;
    private String modifier;
    private String description;
    private String aspect;
    private String source;
    private String biocurator;

    public PhenotypeAnnotation(){
    }

    public PhenotypeAnnotation(String diseaseId, String diseaseName, String hpoId, String hpoName, String ageOfOnset,
                               String evidence, String modifier, String description, String aspect, String source,
                               String biocurator) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.hpoId = hpoId;
        this.hpoName = hpoName;
        this.ageOfOnset = ageOfOnset;
        this.evidence = evidence;
        this.modifier = modifier;
        this.description = description;
        this.aspect = aspect;
        this.source = source;
        this.biocurator = biocurator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getHpoId() {
        return hpoId;
    }

    public void setHpoId(String hpoId) {
        this.hpoId = hpoId;
    }

    public String getHpoName() {
        return hpoName;
    }

    public void setHpoName(String hpoName) {
        this.hpoName = hpoName;
    }

    public String getAgeOfOnset() {
        return ageOfOnset;
    }

    public void setAgeOfOnset(String ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBiocurator() {
        return biocurator;
    }

    public void setBiocurator(String biocurator) {
        this.biocurator = biocurator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhenotypeAnnotation that = (PhenotypeAnnotation) o;
        return Objects.equals(id, that.id) && Objects.equals(diseaseId, that.diseaseId) && Objects.equals(diseaseName, that.diseaseName) && Objects.equals(hpoId, that.hpoId) && Objects.equals(hpoName, that.hpoName) && Objects.equals(ageOfOnset, that.ageOfOnset) && Objects.equals(evidence, that.evidence) && Objects.equals(modifier, that.modifier) && Objects.equals(description, that.description) && Objects.equals(aspect, that.aspect) && Objects.equals(source, that.source) && Objects.equals(biocurator, that.biocurator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, diseaseId, diseaseName, hpoId, hpoName, ageOfOnset, evidence, modifier, description, aspect, source, biocurator);
    }

    @Override
    public String toString() {
        return "RareDiseaseAnnotation{" + "id=" + id + ", diseaseId='" + diseaseId + '\'' + ", diseaseName='" + diseaseName + '\'' + ", hpoId='" + hpoId + '\'' + ", hpoName='" + hpoName + '\'' + ", ageOfOnset='" + ageOfOnset + '\'' + ", evidence='" + evidence + '\'' + ", modifier='" + modifier + '\'' + ", description='" + description + '\'' + ", aspect='" + aspect + '\'' + ", source='" + source + '\'' + ", biocurator='" + biocurator + '\'' + '}';
    }
}
