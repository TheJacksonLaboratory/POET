package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.Annotation;
import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.MaxoAnnotation;
import org.springframework.data.repository.CrudRepository;

public interface MaxoAnnotationRepository extends CrudRepository<MaxoAnnotation, Long> {

    MaxoAnnotation findDistinctByAnnotationSource(AnnotationSource source);

}
