package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.PhenotypeAnnotation;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhenotypeAnnotationRepository extends CrudRepository<PhenotypeAnnotation, Long> {
    List<PhenotypeAnnotation> findAllByAnnotationSourceDiseaseAndStatusNot(Disease disease, AnnotationStatus status);
    PhenotypeAnnotation findDistinctById(long id);
    boolean existsByAnnotationSourceAndHpoIdAndSexAndEvidenceTypeAndAgeOfOnsetAndModifier(AnnotationSource source, String hpoId, String sex, String evidenceType, String ageOfOnset, String modifier);
    boolean existsByAnnotationSourceAndHpoIdAndSexAndEvidenceTypeAndAgeOfOnsetAndModifierAndStatusNot(AnnotationSource source, String hpoId, String sex, String evidenceType, String ageOfOnset, String modifier, AnnotationStatus status);
}