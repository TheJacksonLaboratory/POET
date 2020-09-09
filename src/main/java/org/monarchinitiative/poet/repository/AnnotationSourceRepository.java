package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnnotationSourceRepository extends CrudRepository<AnnotationSource, Long > {

    AnnotationSource findByPublicationAndDisease(Publication publication, Disease disease);
    List<AnnotationSource> findDistinctByPublication(Publication publication);
    List<AnnotationSource> findDistinctByDisease(Disease disease);
}

