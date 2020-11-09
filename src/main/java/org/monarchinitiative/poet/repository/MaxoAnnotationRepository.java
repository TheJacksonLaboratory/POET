package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.MaxoAnnotation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MaxoAnnotationRepository extends CrudRepository<MaxoAnnotation, Long> {

    List<MaxoAnnotation> findDistinctByAnnotationSource(AnnotationSource source);

}
