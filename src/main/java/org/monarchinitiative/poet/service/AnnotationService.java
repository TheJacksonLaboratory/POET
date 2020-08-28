package org.monarchinitiative.poet.service;
import org.monarchinitiative.poet.model.entities.MaxoAnnotation;
import org.monarchinitiative.poet.model.entities.Publication;
import org.monarchinitiative.poet.model.entities.RareDiseaseAnnotation;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.springframework.stereotype.Service;

@Service
public class AnnotationService {

    private PublicationRepository publicationRepository;

    public AnnotationService(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
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
     * @param annotation - a maxo annotation from the client
     * @return created
     */
    public MaxoAnnotation createMaxoAnnotation(MaxoAnnotation annotation) {
        return annotation;
    }

    /*public Annotation createAnnotation(CommonDiseaseAnnotation annotation) {
        return new CommonDiseaseAnnotation();
    }*/

    public Publication createPublication(){
        Publication publication = new Publication("27741350", "Measuring cancer evolution from the genome");
        Publication publication1 = new Publication("3009398", "Confirming biology through biology.");
        publicationRepository.save(publication);
        publicationRepository.save(publication1);
        return publication;
    }

}
