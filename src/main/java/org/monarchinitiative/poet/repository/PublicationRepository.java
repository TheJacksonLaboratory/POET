package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.Publication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PublicationRepository extends CrudRepository<Publication, Long> {

    List<Publication> findByNameContaining(String name);
    List<Publication> findByIdentifierStartingWithOrNameContainingIgnoreCase(String id, String name);
    Publication findByIdentifier(String id);

}
