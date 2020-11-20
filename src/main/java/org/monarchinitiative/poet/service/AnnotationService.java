package org.monarchinitiative.poet.service;
import org.monarchinitiative.poet.model.MaxoRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.repository.AnnotationSourceRepository;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.MaxoAnnotationRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.springframework.stereotype.Service;

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

    /**
     * Create a MaXo annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param maxoRequest - a maxo request body
     * @return boolean created
     */
    public boolean createMaxoAnnotation(MaxoRequest maxoRequest) {
        Publication publication = publicationRepository.findByIdentifier(maxoRequest.getPublicationIdentifier());
        Disease disease = diseaseRepository.findDiseaseByIdentifier(maxoRequest.getDiseaseId());
        AnnotationSource annotationSource;
        if(disease != null && publication != null){
            // We have a valid publication and a valid disease, do we have an annotation source for them?
            annotationSource = annotationSourceRepository.findByPublicationAndDisease(publication, disease);
            if(annotationSource == null){
                // Couldn't find a valid source, create it and continue.
                annotationSource = new AnnotationSource(publication, disease);
                annotationSourceRepository.save(annotationSource);
            }
            MaxoAnnotation annotation = new MaxoAnnotation(maxoRequest, annotationSource);
            maxoAnnotationRepository.save(annotation);
            return true;
        }
        return false;
    }


    /*public Publication createPublication(){
        Publication publication = new Publication("27741350", "Measuring cancer evolution from the genome");
        Publication publication1 = new Publication("3009398", "Confirming biology through biology.");
        publicationRepository.save(publication);
        publicationRepository.save(publication1);
        return publication;
    }*/

}
