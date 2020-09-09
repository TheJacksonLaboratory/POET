package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.Disease;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiseaseRepository extends CrudRepository<Disease, Long> {

    List<Disease> searchDiseasesByDiseaseNameContaining(String name);
    List<Disease> searchDiseasesByDiseaseIdContaining(String id);
    List<Disease> findDiseaseByDiseaseNameContainingIgnoreCaseOrDiseaseIdContainingIgnoreCase(String name, String id);
    Disease findDiseaseByDiseaseId(String id);
    Disease findDiseaseByDiseaseName(String name);
}
