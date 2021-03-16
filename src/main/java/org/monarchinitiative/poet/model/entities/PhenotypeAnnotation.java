package org.monarchinitiative.poet.model.entities;

import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.requests.PhenotypeRequest;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@DiscriminatorValue("phenotype")
public class PhenotypeAnnotation extends Annotation {

    private String hpoId;
    private String hpoName;
    private String ageOfOnset;
    private String evidenceType;
    private String modifier;
    private String frequency;
    private String description;
    private String sex;
    private String biocurator;

    public PhenotypeAnnotation(){
    }

    public PhenotypeAnnotation(String hpoId, String hpoName, String ageOfOnset, String evidenceType,
                               String modifier, String frequency, String description, String sex) {
        this.hpoId = hpoId;
        this.hpoName = hpoName;
        this.ageOfOnset = ageOfOnset;
        this.evidenceType = evidenceType;
        this.modifier = modifier;
        this.frequency = frequency;
        this.description = description;
        this.sex = sex;
    }

    public PhenotypeAnnotation(AnnotationSource annotationSource, AnnotationStatus status, String hpoId,
                               String hpoName, String ageOfOnset, String evidenceType, String modifier,
                               String frequency, String description, String sex) {
        super(annotationSource, status);
        this.hpoId = hpoId;
        this.hpoName = hpoName;
        this.ageOfOnset = ageOfOnset;
        this.evidenceType = evidenceType;
        this.modifier = modifier;
        this.frequency = frequency;
        this.description = description;
        this.sex = sex;
    }

    public PhenotypeAnnotation(PhenotypeRequest phenotypeRequest, AnnotationSource annotationSource,
                               AnnotationStatus status) {
        super(annotationSource, status);
        this.hpoId = phenotypeRequest.getHpoId();
        this.hpoName = phenotypeRequest.getHpoName();
        this.ageOfOnset = phenotypeRequest.getAgeOfOnset();
        this.evidenceType = phenotypeRequest.getEvidenceType();
        this.modifier = phenotypeRequest.getModifiers();
        this.frequency = phenotypeRequest.getFrequency();
        this.description = phenotypeRequest.getDescription();
        this.sex = phenotypeRequest.getSex();
    }

    public String getHpoId() {
        return hpoId;
    }

    public String getHpoName() {
        return hpoName;
    }

    public String getAgeOfOnset() {
        return ageOfOnset;
    }

    public String getEvidenceType() {
        return evidenceType;
    }

    public String getModifier() {
        return modifier;
    }

    public String getDescription() {
        return description;
    }

    public String getSex() { return sex; }

    public void setBiocurator(String biocurator) {
        this.biocurator = biocurator;
    }

}
