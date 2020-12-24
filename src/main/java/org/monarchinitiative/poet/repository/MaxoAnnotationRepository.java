package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.MaxoAnnotation;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MaxoAnnotationRepository extends CrudRepository<MaxoAnnotation, Long> {

    List<MaxoAnnotation> findDistinctByAnnotationSourceAndStatusNot(AnnotationSource source, AnnotationStatus status);
    List<MaxoAnnotation> findAllByAnnotationSourceDiseaseAndStatusNot(Disease disease, AnnotationStatus status);
    MaxoAnnotation findDistinctById(long id);

}
