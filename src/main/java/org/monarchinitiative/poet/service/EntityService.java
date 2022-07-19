package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.exceptions.AnnotationSourceException;
import org.monarchinitiative.poet.exceptions.DiseaseNotFoundException;
import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.model.requests.DiseaseRequest;
import org.monarchinitiative.poet.model.requests.PublicationRequest;
import org.monarchinitiative.poet.repository.AnnotationSourceRepository;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public EntityService(DiseaseRepository diseaseRepository, PublicationRepository publicationRepository,
                         AnnotationSourceRepository annotationSourceRepository) {
        this.diseaseRepository = diseaseRepository;
        this.publicationRepository = publicationRepository;
        this.annotationSourceRepository = annotationSourceRepository;
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
     * A function to get fetch all publications in the database
     *
     * @return a list of publications
     * @since 0.5.0
     */
    public List<Publication> getAllPublications(){
        return (List<Publication>) this.publicationRepository.findAll();
    }

    /**
     * A function to update a publication given the updated publication
     *
     *
     * @since 0.7.0
     */
    public void updatePublication(Publication publication){
        this.publicationRepository.save(publication);
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
        final Publication publication = this.publicationRepository.findByPublicationId(publicationId);
        final Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(diseaseId);
        if(disease != null && publication != null) {
            try{
                return this.annotationSourceRepository.findByPublicationAndDisease(publication, disease);
            } catch (Exception ex){
                return null;
            }

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
            AnnotationSource source = this.annotationSourceRepository.findByPublicationAndDisease(publication, disease);
            if(source != null){
                throw AnnotationSourceException.exists(publication.getPublicationId(), disease.getDiseaseId());
            }
            return this.annotationSourceRepository.save(new AnnotationSource(publication, disease));
        } else {
            throw new DiseaseNotFoundException(request.getDisease().getDiseaseId());
        }
    }
}
