package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EntityService {
    private final DiseaseRepository diseaseRepository;
    private final PublicationRepository publicationRepository;

    public EntityService(DiseaseRepository diseaseRepository, PublicationRepository publicationRepository) {
        this.diseaseRepository = diseaseRepository;
        this.publicationRepository = publicationRepository;
    }

    /**
     * Fetch a Disease object from the database with the supplied id.
     * @param id - An OMIM identifier.
     * @return Disease or null
     */
    public Disease getDisease(String id){
        return this.diseaseRepository.findDiseaseByDiseaseId(id);
    }

    public List<Publication> getDiseasePublications(String id){
        Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(id);
        if(disease != null){
            return disease.getPublications();
        } else {
            return Collections.emptyList();
        }
    }

    public Publication getPublication(String id){
        return this.publicationRepository.findByPublicationId(id);
    }

    public List<Disease> getPublicationDiseases(String id){
        Publication publication = this.publicationRepository.findByPublicationId(id);
        if(publication != null) {
            return publication.getDiseases();
        } else {
            return Collections.emptyList();
        }
    }


}
