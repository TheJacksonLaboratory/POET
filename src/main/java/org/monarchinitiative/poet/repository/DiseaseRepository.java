package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.Disease;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiseaseRepository extends CrudRepository<Disease, Long> {

    List<Disease> findDiseasesByNameContaining(String name);
    List<Disease> findDiseasesByIdentifierContaining(String id);
    List<Disease> findDiseaseByNameContainingIgnoreCaseOrIdentifierContainingIgnoreCase(String name, String id);
    Disease findDiseaseByIdentifier(String id);
    Disease findDiseaseByName(String name);
}
