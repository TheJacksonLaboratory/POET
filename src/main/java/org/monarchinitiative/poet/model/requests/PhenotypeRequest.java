package org.monarchinitiative.poet.model.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * The model for the JSON requests for phenotype endpoints with validation
 */
public class PhenotypeRequest {

    private final Long id;
    @NotNull
    private final String hpoName;
    @NotNull
    @Pattern(regexp = "HP:[0-9]{7}$", message = "hpoId should be a valid identifier from human phenotype ontology")
    private final String hpoId;
    @NotNull
    private final String evidence;
    private final String description;
    @Pattern(regexp = "HP:[0-9]{7}$", message = "hpoId should be a valid identifier from human phenotype ontology")
    private final String onset;
    private final String publicationId;
    private final String publicationName;
    private final String diseaseId;
    private final String diseaseName;
    private final String modifiers;
    private final String qualifier;
    private final String frequency;
    @Pattern(regexp = "MALE|FEMALE", message = "sex be one of (MALE, FEMALE)")
    private final String sex;
    private final String message;

    public PhenotypeRequest(Long id, String hpoName, String hpoId, String evidence, String description,
                            String onset, String publicationId, String publicationName,
                            String diseaseId, String diseaseName, String modifiers,
                            String frequency, String qualifier, String sex, String message) {
        this.id = id;
        this.hpoName = hpoName;
        this.hpoId = hpoId;
        this.evidence = evidence;
        this.onset = onset;
        this.description = description;
        this.publicationId = publicationId;
        this.publicationName = publicationName;
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.modifiers = modifiers;
        this.qualifier = qualifier;
        this.frequency = frequency;
        this.sex = sex;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getHpoName() {
        return hpoName;
    }

    public String getHpoId() {
        return hpoId;
    }

    public String getEvidence() {
        return evidence;
    }

    public String getOnset() {
        return onset;
    }

    public String getDescription() {
        return description;
    }

    public String getPublicationId() {
        return publicationId;
    }

    public String getPublicationName() {
        return publicationName;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getModifiers() {
        return modifiers;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getSex() {
        return sex;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhenotypeRequest that = (PhenotypeRequest) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(hpoName, that.hpoName) &&
                Objects.equals(hpoId, that.hpoId) &&
                Objects.equals(evidence, that.evidence) &&
                Objects.equals(description, that.description) &&
                Objects.equals(onset, that.onset) &&
                Objects.equals(publicationId, that.publicationId) &&
                Objects.equals(publicationName, that.publicationName) &&
                Objects.equals(diseaseId, that.diseaseId) &&
                Objects.equals(diseaseName, that.diseaseName) &&
                Objects.equals(modifiers, that.modifiers) &&
                Objects.equals(qualifier, that.qualifier) &&
                Objects.equals(frequency, that.frequency) &&
                Objects.equals(sex, that.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hpoName, hpoId, evidence, description, onset, publicationId, publicationName, diseaseId, diseaseName, modifiers, qualifier, frequency, sex);
    }
}
