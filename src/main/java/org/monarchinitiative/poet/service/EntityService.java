package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.exceptions.AuthenticationException;
import org.monarchinitiative.poet.exceptions.DiseaseNotFoundException;
import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.model.entities.User;
import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.model.requests.PublicationRequest;
import org.monarchinitiative.poet.repository.AnnotationSourceRepository;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.monarchinitiative.poet.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EntityService {
    private final DiseaseRepository diseaseRepository;
    private final PublicationRepository publicationRepository;
    private final AnnotationSourceRepository annotationSourceRepository;
    private final UserRepository userRepository;

    public EntityService(DiseaseRepository diseaseRepository, PublicationRepository publicationRepository,
                         AnnotationSourceRepository annotationSourceRepository, UserRepository userRepository) {
        this.diseaseRepository = diseaseRepository;
        this.publicationRepository = publicationRepository;
        this.annotationSourceRepository = annotationSourceRepository;
        this.userRepository = userRepository;
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

    public boolean savePublicationToDisease(PublicationRequest request, Authentication authentication){
        Publication publication = new Publication(request.getPublication());
        Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(request.getDisease().getDiseaseId());
        User user = userRepository.findDistinctByAuthId(authentication.getName());
        if(disease != null){
            if(user != null && user.getCurationRole().equals(CurationRole.ELEVATED_CURATOR)){
                this.publicationRepository.save(publication);
                this.annotationSourceRepository.save(new AnnotationSource(publication, disease));
                return true;
            } else {
                throw new AuthenticationException(authentication.getName());
            }
        } else {
            throw new DiseaseNotFoundException(request.getDisease().getDiseaseId());
        }
    }
}
