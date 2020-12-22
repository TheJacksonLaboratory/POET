package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * A spring service component created to provide business logic and functionality to manage main poet entities.
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@Service
public class EntityService {
    private final DiseaseRepository diseaseRepository;
    private final PublicationRepository publicationRepository;

    public EntityService(DiseaseRepository diseaseRepository, PublicationRepository publicationRepository) {
        this.diseaseRepository = diseaseRepository;
        this.publicationRepository = publicationRepository;
    }

    /**
     * A function to get a disease from the disease repository implementation
     *
     * @param id a OMIM disease id
     *
     * @return a disease or nothing
     * @since 0.5.0
     */
    public Disease getDisease(String id){
        return this.diseaseRepository.findDiseaseByDiseaseId(id);
    }

    /**
     * A function to get disease publications from the
     *
     * @param id a OMIM disease id
     *
     * @return a list of publications or an empty list
     * @since 0.5.0
     */
    public List<Publication> getDiseasePublications(String id){
        Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(id);
        if(disease != null){
            return disease.getPublications();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * A function to get a publication from the publication repository
     *
     * @param id a PubMed id
     *
     * @return a publication or nothing
     * @since 0.5.0
     */
    public Publication getPublication(String id){
        return this.publicationRepository.findByPublicationId(id);
    }

    /**
     * A function to get a diseases associated to a publication from the publication repository
     *
     * @param id a PubMed id
     *
     * @return a list of diseases or and empty list
     * @since 0.5.0
     */
    public List<Disease> getPublicationDiseases(String id){
        Publication publication = this.publicationRepository.findByPublicationId(id);
        if(publication != null) {
            return publication.getDiseases();
        } else {
            return Collections.emptyList();
        }
    }
}
