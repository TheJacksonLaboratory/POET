package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.MaxoRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.model.enumeration.CurationAction;
import org.monarchinitiative.poet.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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

    public List<MaxoAnnotation> getMaxoAnnotation(String diseaseId, String publicationId, String sort) {
            // If publication get source, then annotations for that source
            // Otherwise get all annotationSource
            if(publicationId != null){
                final AnnotationSource annotationSource = getAnnotationSource(publicationId, diseaseId);
                if(annotationSource != null){
                    List<MaxoAnnotation> annotations = this.maxoAnnotationRepository.findDistinctByAnnotationSource(annotationSource);
                    if(annotations.size() > 0){
                        return annotations;
                    } else {
                        return Collections.emptyList();
                    }
                }
            } else {
                Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(diseaseId);
                if(disease != null) {
                    List<MaxoAnnotation> annotations = this.maxoAnnotationRepository.findAllByAnnotationSourceDisease(disease);
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
     * @param maxoRequest - a maxo request body
     * @return boolean created
     */
    public boolean createMaxoAnnotation(MaxoRequest maxoRequest, Authentication authentication) {
        // We have a valid publication and a valid disease, do we have an annotation source for them?
        if(maxoRequest != null){
            final AnnotationSource annotationSource = getAnnotationSource(maxoRequest.getPublicationId(), maxoRequest.getDiseaseId());
            if(annotationSource != null){
                final MaxoAnnotation annotation = new MaxoAnnotation(maxoRequest, annotationSource);
                maxoAnnotationRepository.save(annotation);
                updateUserActivity(authentication, CurationAction.CREATE, annotation);
                return true;
            }
        }
        return false;
    }

    private AnnotationSource getAnnotationSource(String publicationId, String diseaseId){
        final Publication publication = publicationRepository.findByPublicationId(publicationId);
        final Disease disease = diseaseRepository.findDiseaseByDiseaseId(diseaseId);
        if(disease !=null && publication != null) {
            return annotationSourceRepository.findByPublicationAndDisease(publication, disease);
        }
        return null;
    }

    private void updateUserActivity(Authentication authentication, CurationAction curationAction, Annotation annotation){
          User user = userRepository.findDistinctByAuthId(authentication.getName());
          if(user != null){
              UserActivity userActivity = new UserActivity(user, curationAction, annotation);
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
