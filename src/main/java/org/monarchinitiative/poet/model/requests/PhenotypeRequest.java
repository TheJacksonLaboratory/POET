package org.monarchinitiative.poet.model.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    @Pattern(regexp = "PMID:[0-9]{8}", message = "publicationID should be a valid identifier from PubMed")
    private final String publicationId;
    private final String publicationName;
    @Pattern(regexp = "MONDO:[0-9]{7}", message = "diseaseId should be a valid identifier from mondo disease ontology")
    private final String diseaseId;
    private final String diseaseName;
    private final String modifiers;
    private final String qualifier;
    @Pattern(regexp = "\\d+\\/\\d+", message = "frequency must be N / M where N is greater than M.")
    private final String frequency;
    @Pattern(regexp = "MALE|FEMALE", message = "sex be one of (MALE, FEMALE)")
    private final String sex;

    public PhenotypeRequest(Long id, String hpoName, String hpoId, String evidence, String description,
                            String onset, String publicationId, String publicationName,
                            String diseaseId, String diseaseName, String modifiers,
                            String frequency, String qualifier, String sex) {
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
}
