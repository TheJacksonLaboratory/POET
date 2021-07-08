package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.exceptions.AuthenticationException;
import org.monarchinitiative.poet.exceptions.DiseaseNotFoundException;
import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.model.entities.User;
import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.model.requests.DiseaseRequest;
import org.monarchinitiative.poet.model.requests.PublicationRequest;
import org.monarchinitiative.poet.repository.AnnotationSourceRepository;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.monarchinitiative.poet.repository.UserRepository;
import org.springframework.security.core.Authentication;
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
     * A function to get a disease from the disease repository implementation
     *
     * @param diseaseRequest a disease from the client
     *
     * @return a disease or nothing
     * @since 0.7.0
     */
    public boolean updateDisease(DiseaseRequest diseaseRequest){
        Disease disease = this.getDisease(diseaseRequest.getDiseaseId());
        disease.setDescription(diseaseRequest.getDescription());
        disease.setEquivalentId(diseaseRequest.getEquivalentId());
        this.diseaseRepository.save(disease);
        return true;
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
     * A function to get a disease from the disease repository implementation
     *
     * @param disease a disease object created from the
     *
     * @return a disease or nothing
     * @since 0.5.0
     */
    public boolean saveNewDisease(Disease disease){
        if(disease.getEquivalentId() != null){
            Disease diseaseOld = this.diseaseRepository.findDiseaseByDiseaseId(disease.getEquivalentId());
            this.diseaseRepository.save(disease);
            if(diseaseOld != null){
                List<AnnotationSource> sourceList = this.annotationSourceRepository.findDistinctByDisease(diseaseOld);
                sourceList.forEach(source -> {
                    source.setDisease(disease);
                    this.annotationSourceRepository.save(source);
                });
            }
        } else {
            this.diseaseRepository.save(disease);
        }
        return true;
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

    /**
     * A function to get an annotation source object.
     *
     * @param publicationId a PubMed id
     * @param diseaseId a OMIM disease id
     *
     * @return an annotation source object  or null
     * @since 0.5.0
     */
    public AnnotationSource getAnnotationSource(String publicationId, String diseaseId){
        final Publication publication = publicationRepository.findByPublicationId(publicationId);
        final Disease disease = diseaseRepository.findDiseaseByDiseaseId(diseaseId);
        if(disease != null) {
            return annotationSourceRepository.findByPublicationAndDisease(publication, disease);
        }
        return null;
    }

    /**
     * A function to save a publication annotation to a disease.
     *
     * @param request a publication request to save to a disease
     */
    public AnnotationSource createAnnotationSource(PublicationRequest request){
        Publication publication = this.publicationRepository.findByPublicationId(request.getPublication().getPublicationId());
        Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(request.getDisease().getDiseaseId());
        if(publication == null){
            publication = new Publication(request.getPublication());
            this.publicationRepository.save(publication);
        }

        if(disease != null){
            return this.annotationSourceRepository.save(new AnnotationSource(publication, disease));
        } else {
            throw new DiseaseNotFoundException(request.getDisease().getDiseaseId());
        }
    }

    /**
     * A function to create a disease identifier source. ( an annotation source without a publication
     * we assume the source is the disease itself, either orphanet or omim )
     *
     * @param disease - the disease object to see if it has a self source.
     *
     * @return an annotation source
     */
    public AnnotationSource createOrGetDiseaseDatabaseSource(Disease disease){
        final AnnotationSource source = this.annotationSourceRepository.findByPublicationAndDisease(null, disease);
        if(source == null){
            return this.annotationSourceRepository.save(new AnnotationSource(null, disease));
        } else {
            return source;
        }
    }
}
