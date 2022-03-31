package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.requests.PhenotypeRequest;
import org.monarchinitiative.poet.views.AnnotationViews;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("phenotype")
public class PhenotypeAnnotation extends Annotation {

    @JsonView(AnnotationViews.Simple.class)
    private String hpoId;
    @JsonView(AnnotationViews.Simple.class)
    private String hpoName;
    @JsonView(AnnotationViews.Simple.class)
    private String onset;
    @JsonView(AnnotationViews.Simple.class)
    private String evidence;
    @JsonView(AnnotationViews.Simple.class)
    private String modifier;
    @JsonView(AnnotationViews.Simple.class)
    private String frequency;
    @JsonView(AnnotationViews.Simple.class)
    private String qualifier;
    @JsonView(AnnotationViews.Simple.class)
    private String description;
    @JsonView(AnnotationViews.Simple.class)
    private String sex;

    public PhenotypeAnnotation(){
    }

    public PhenotypeAnnotation(String hpoId, String hpoName, String onset, String evidence,
                               String modifier, String frequency, String qualifier, String description, String sex) {
        this.hpoId = hpoId;
        this.hpoName = hpoName;
        this.onset = onset;
        this.evidence = evidence;
        this.modifier = modifier;
        this.frequency = frequency;
        this.description = description;
        this.qualifier = qualifier;
        this.sex = sex;
    }

    public PhenotypeAnnotation(PhenotypeRequest phenotypeRequest, AnnotationSource annotationSource,
                               AnnotationStatus status, User owner) {
        super(annotationSource, status, owner);
        this.hpoId = phenotypeRequest.getHpoId();
        this.hpoName = phenotypeRequest.getHpoName();
        this.onset = phenotypeRequest.getOnset();
        this.evidence = phenotypeRequest.getEvidence();
        this.modifier = phenotypeRequest.getModifiers();
        this.frequency = phenotypeRequest.getFrequency();
        this.qualifier = phenotypeRequest.getQualifier();
        this.description = phenotypeRequest.getDescription();
        this.sex = phenotypeRequest.getSex();
    }

    public void updateAnnotation(PhenotypeRequest phenotypeRequest, AnnotationSource annotationSource){
        this.hpoId = phenotypeRequest.getHpoId();
        this.hpoName = phenotypeRequest.getHpoName();
        this.onset = phenotypeRequest.getOnset();
        this.evidence = phenotypeRequest.getEvidence();
        this.modifier = phenotypeRequest.getModifiers();
        this.frequency = phenotypeRequest.getFrequency();
        this.qualifier = phenotypeRequest.getQualifier();
        this.description = phenotypeRequest.getDescription();
        this.sex = phenotypeRequest.getSex();
        this.setAnnotationSource(annotationSource);
    }

    public String getHpoId() {
        return hpoId;
    }

    public String getHpoName() {
        return hpoName;
    }

    public String getOnset() {
        return onset;
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

    public String getSex() { return sex; }

    public String getFrequency() { return frequency; }

    public String getQualifier() { return qualifier; }

}
