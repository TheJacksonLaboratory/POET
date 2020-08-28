package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.Publication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PublicationRepository extends CrudRepository<Publication, Long> {

    List<Publication> findByPublicationNameContaining(String name);
    List<Publication> findByPublicationIdentifierStartingWithOrPublicationNameContainingIgnoreCase(String id, String name);
    Publication findByPublicationIdentifier(Integer id);

}
