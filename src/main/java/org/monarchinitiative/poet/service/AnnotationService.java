package org.monarchinitiative.poet.service;
import org.monarchinitiative.poet.model.MaxoRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.repository.AnnotationSourceRepository;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.MaxoAnnotationRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AnnotationService {

    private PublicationRepository publicationRepository;
    private DiseaseRepository diseaseRepository;
    private AnnotationSourceRepository annotationSourceRepository;
    private MaxoAnnotationRepository maxoAnnotationRepository;

    public AnnotationService(PublicationRepository publicationRepository,
                             DiseaseRepository diseaseRepository, AnnotationSourceRepository annotationSourceRepository,
                             MaxoAnnotationRepository maxoAnnotationRepository) {
        this.publicationRepository = publicationRepository;
        this.diseaseRepository = diseaseRepository;
        this.annotationSourceRepository = annotationSourceRepository;
        this.maxoAnnotationRepository = maxoAnnotationRepository;
    }

    /**
     * Create a rare annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param annotation - a rare annotation from the client
     * @return created
     */
    public RareDiseaseAnnotation createRareDiseaseAnnotation(RareDiseaseAnnotation annotation) {
        // Check fields / annotations format
        // insert into annotation field
        // link up annotation id in rare disease table
        // get user
        // update users activity table with annotation
        return annotation;
    }

    public RareDiseaseAnnotation getRareDiseaseAnnotation(String disease){
        return new RareDiseaseAnnotation();
    }

    public List<MaxoAnnotation> getMaxoAnnotation(String publicationId, String diseaseId, String sort) {
            final AnnotationSource annotationSource = getAnnotationSource(publicationId, diseaseId);
            if(annotationSource != null){
                List<MaxoAnnotation> annotations = this.maxoAnnotationRepository.findDistinctByAnnotationSource(annotationSource);
                if(annotations.size() > 0){
                    return annotations;
                } else {
                    return Collections.emptyList();
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
    public boolean createMaxoAnnotation(MaxoRequest maxoRequest) {
        // We have a valid publication and a valid disease, do we have an annotation source for them?
        final AnnotationSource annotationSource = getAnnotationSource(maxoRequest.getPublicationId(),maxoRequest.getDiseaseId());
        if(annotationSource != null){
            final MaxoAnnotation annotation = new MaxoAnnotation(maxoRequest, annotationSource);
            maxoAnnotationRepository.save(annotation);
            return true;
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


    /*public Publication createPublication(){
        Publication publication = new Publication("27741350", "Measuring cancer evolution from the genome");
        Publication publication1 = new Publication("3009398", "Confirming biology through biology.");
        publicationRepository.save(publication);
        publicationRepository.save(publication1);
        return publication;
    }*/

}
