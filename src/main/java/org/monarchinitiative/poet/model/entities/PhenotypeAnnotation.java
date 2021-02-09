package org.monarchinitiative.poet.model.entities;

import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@DiscriminatorValue("phenotype")
public class PhenotypeAnnotation extends Annotation {
    private @Id @GeneratedValue Long id;
    private String hpoId;
    private String hpoName;
    private String ageOfOnset;
    private String evidence;
    private String modifier;
    private String description;
    private String aspect;

    public PhenotypeAnnotation(){
    }

    public PhenotypeAnnotation(String hpoId, String hpoName, String ageOfOnset,
                               String evidence, String modifier, String description, String aspect) {
        this.hpoId = hpoId;
        this.hpoName = hpoName;
        this.ageOfOnset = ageOfOnset;
        this.evidence = evidence;
        this.modifier = modifier;
        this.description = description;
        this.aspect = aspect;
    }

    public PhenotypeAnnotation(AnnotationSource source, AnnotationStatus status,
                               String hpoId, String hpoName, String ageOfOnset,
                               String evidence, String modifier, String description, String aspect){
        super(source, status);
        this.hpoId = hpoId;
        this.hpoName = hpoName;
        this.ageOfOnset = ageOfOnset;
        this.evidence = evidence;
        this.modifier = modifier;
        this.description = description;
        this.aspect = aspect;
    }

    public Long getId() {
        return id;
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

    public String getEvidence() {
        return evidence;
    }

    public String getModifier() {
        return modifier;
    }

    public String getDescription() {
        return description;
    }

    public String getAspect() {
        return aspect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PhenotypeAnnotation that = (PhenotypeAnnotation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(hpoId, that.hpoId) &&
                Objects.equals(hpoName, that.hpoName) &&
                Objects.equals(ageOfOnset, that.ageOfOnset) &&
                Objects.equals(evidence, that.evidence) &&
                Objects.equals(modifier, that.modifier) &&
                Objects.equals(description, that.description) &&
                Objects.equals(aspect, that.aspect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, hpoId, hpoName, ageOfOnset, evidence, modifier, description, aspect);
    }

    @Override
    public String toString() {
        return "PhenotypeAnnotation{" +
                "id=" + id +
                ", hpoId='" + hpoId + '\'' +
                ", hpoName='" + hpoName + '\'' +
                ", ageOfOnset='" + ageOfOnset + '\'' +
                ", evidence='" + evidence + '\'' +
                ", modifier='" + modifier + '\'' +
                ", description='" + description + '\'' +
                ", aspect='" + aspect + '\'' +
                '}';
    }
}
