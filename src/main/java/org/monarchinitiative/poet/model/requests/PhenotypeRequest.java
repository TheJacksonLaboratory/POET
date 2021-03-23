package org.monarchinitiative.poet.model.requests;

public class PhenotypeRequest {

    private final Long id;
    private final String hpoName;
    private final String hpoId;
    private final String evidence;
    private final String description;
    private final String onset;
    private final String publicationId;
    private final String publicationName;
    private final String diseaseId;
    private final String diseaseName;
    private final String modifiers;
    private final String qualifier;
    private final String frequency;
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
