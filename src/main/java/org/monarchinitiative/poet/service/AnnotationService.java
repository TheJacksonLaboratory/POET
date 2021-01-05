package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.requests.MaxoRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.model.enumeration.CurationAction;
import org.monarchinitiative.poet.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * A spring service component created to provide business logic and functionality to manage annotations
 * for poet entities.
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@Service
public class AnnotationService {

    private PublicationRepository publicationRepository;
    private DiseaseRepository diseaseRepository;
    private AnnotationSourceRepository annotationSourceRepository;
    private MaxoAnnotationRepository maxoAnnotationRepository;
    private UserRepository userRepository;
    private UserActivityRepository userActivityRespository;

    public AnnotationService(PublicationRepository publicationRepository,
                             DiseaseRepository diseaseRepository, AnnotationSourceRepository annotationSourceRepository,
                             MaxoAnnotationRepository maxoAnnotationRepository, UserRepository userRepository,
                             UserActivityRepository userActivityRepository) {
        this.publicationRepository = publicationRepository;
        this.diseaseRepository = diseaseRepository;
        this.annotationSourceRepository = annotationSourceRepository;
        this.maxoAnnotationRepository = maxoAnnotationRepository;
        this.userRepository = userRepository;
        this.userActivityRespository = userActivityRepository;
    }

    /**
     * A function to get maxo annotations from the database by either both publication and  disease or just disease.
     *
     * @param diseaseId a OMIM disease id
     * @param publicationId a PubMed id
     * @param sort a string composing of two parts both direction and field. TODO: Implement functionality
     *
     * @return a collection of maxo annotations or an empty list.
     * @since 0.5.0
     */
    public List<MaxoAnnotation> getMaxoAnnotation(String diseaseId, String publicationId, String sort) {
            // If publication get source, then annotations for that source
            // Otherwise get all annotationSource
            if(publicationId != null){
                final AnnotationSource annotationSource = getAnnotationSource(publicationId, diseaseId);
                if(annotationSource != null){
                    List<MaxoAnnotation> annotations = this.maxoAnnotationRepository.findDistinctByAnnotationSourceAndStatusNot(annotationSource, AnnotationStatus.RETIRED);
                    if(annotations.size() > 0){
                        return annotations;
                    } else {
                        return Collections.emptyList();
                    }
                }
            } else {
                Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(diseaseId);
                if(disease != null) {
                    List<MaxoAnnotation> annotations = this.maxoAnnotationRepository.findAllByAnnotationSourceDiseaseAndStatusNot(disease, AnnotationStatus.RETIRED);
                    if(annotations.size() > 0){
                        return annotations;
                    } else {
                        return Collections.emptyList();
                    }
                } else {
                    return null;
                }
            }
            return null;
    }

    /**
     * Create a MaXo annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param maxoRequest a maxo request body
     *
     * @return a boolean whether the annotation was created or not.
     */
    public boolean createMaxoAnnotation(MaxoRequest maxoRequest, Authentication authentication) {
        // We have a valid publication and a valid disease, do we have an annotation source for them?
        if(maxoRequest != null){
            final AnnotationSource annotationSource = getAnnotationSource(maxoRequest.getPublicationId(), maxoRequest.getDiseaseId());
            if(annotationSource != null){
                final MaxoAnnotation annotation = new MaxoAnnotation(maxoRequest, annotationSource);
                maxoAnnotationRepository.save(annotation);
                updateUserActivity(authentication, CurationAction.CREATE, annotation, null);
                return true;
            }
        }
        return false;
    }

    /**
     * Update a MaXo annotation enforcing business rules with the status of the annotation
     *
     * @param maxoRequest a maxo request body
     *
     * @return a boolean whether the annotation was created or not.
     */
    public boolean updateMaxoAnnotation(MaxoRequest maxoRequest, Authentication authentication) {
        // We have a valid publication and a valid disease, do we have an annotation source for them?
        // Validate that who is updating owns the annotation or is super elevated curator
        if(maxoRequest != null){
            // Retired the old annotation
            MaxoAnnotation oldAnnotation = maxoAnnotationRepository.findDistinctById(maxoRequest.getId());
            // Create the new one with updated values
            final MaxoAnnotation annotation = new MaxoAnnotation(oldAnnotation.getAnnotationSource(),
                    oldAnnotation.getStatus(), maxoRequest.getMaxoId(), maxoRequest.getMaxoName(),
                    maxoRequest.getHpoName(), maxoRequest.getHpoId(), maxoRequest.getEvidence(),
                    maxoRequest.getComment(), maxoRequest.getRelation(), maxoRequest.getExtension());
            maxoAnnotationRepository.save(oldAnnotation);
            oldAnnotation.setStatus(AnnotationStatus.RETIRED);
            maxoAnnotationRepository.save(annotation);
            updateUserActivity(authentication, CurationAction.UPDATE, annotation, oldAnnotation);
            return true;
        }
        return false;
    }

    /**
     * Delete a MaXo annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param id a maxo annotation id
     *
     * @return a boolean whether the annotation was created or not.
     */
    public boolean deleteMaxoAnnotation(Long id, Authentication authentication) {
        if(id != null){
            MaxoAnnotation annotation = maxoAnnotationRepository.findDistinctById(id);
            if(annotation != null){
                annotation.setStatus(AnnotationStatus.RETIRED);
                updateUserActivity(authentication, CurationAction.DELETE, annotation, null);
                return true;
            }
        }
        return false;
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
    private AnnotationSource getAnnotationSource(String publicationId, String diseaseId){
        final Publication publication = publicationRepository.findByPublicationId(publicationId);
        final Disease disease = diseaseRepository.findDiseaseByDiseaseId(diseaseId);
        if(disease !=null && publication != null) {
            return annotationSourceRepository.findByPublicationAndDisease(publication, disease);
        }
        return null;
    }

    /**
     * A function to track user activity that happens when creating, editing or deleting annotations.
     *
     * @param authentication the spring authentication object.
     * @param curationAction the action that a user is taking.
     * @param annotation the annotation that is being modified.
     *
     * @since 0.5.0
     */
    private void updateUserActivity(Authentication authentication, CurationAction curationAction,
                                    MaxoAnnotation annotation, MaxoAnnotation oldAnnotation){
          User user = userRepository.findDistinctByAuthId(authentication.getName());
          if(user != null){
              UserActivity userActivity = new UserActivity(user, curationAction, annotation, oldAnnotation);
              userActivityRespository.save(userActivity);
          } else {
              // We got a sec leak
              System.out.println("Rut Roh!");
          }
    }


    public void insertTestData(){
        Publication publication = new Publication("PMID:31479590", "Encoding Clinical Data with the Human Phenotype Ontology for Computational Differential Diagnostics.", "2019 Sept", "Kohler S");
        Publication publication2 = new Publication("PMID:30476213", "Expansion of the Human Phenotype Ontology (HPO) knowledge base and resources", "2019 Jan", "Kohler S");
        Publication publication3 = new Publication("PMID:30323234", "Mikes future first author paper", "2020 Jan", "Gargano M");
        publicationRepository.save(publication);
        publicationRepository.save(publication2);
        publicationRepository.save(publication3);
        Disease disease = new Disease("OMIM:154700", "Marfan Syndrome");
        Disease disease2 = new Disease("OMIM:300200", "Adrenal Hypoplasia, Congenital");
        diseaseRepository.save(disease);
        diseaseRepository.save(disease2);
        annotationSourceRepository.save(new AnnotationSource(publication3, disease));
        annotationSourceRepository.save(new AnnotationSource(publication, disease));
        annotationSourceRepository.save(new AnnotationSource(publication2, disease));
    }
}
