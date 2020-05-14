package org.monarchinitiative.poet.service;
import org.monarchinitiative.poet.model.MaxoAnnotation;
import org.monarchinitiative.poet.model.RareDiseaseAnnotation;
import org.springframework.stereotype.Service;

@Service
public class AnnotationService {

    /**
     * Create a rare annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param annotation - a rare annotation from the client
     * @return created
     */
    public RareDiseaseAnnotation createRareAnnotation(RareDiseaseAnnotation annotation) {
        // Check fields / annotations format
        // insert into annotation field
        // link up annotation id in rare disease table
        // get user
        // update users activity table with annotation
        return annotation;
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

}
