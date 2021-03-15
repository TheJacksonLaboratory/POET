package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.PhenotypeAnnotation;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;

import java.util.List;

public interface PhenotypeAnnotationRepository {
    List<PhenotypeAnnotation> findAllByAnnotationSourceDiseaseAndStatusNot(Disease disease, AnnotationStatus status);
}
