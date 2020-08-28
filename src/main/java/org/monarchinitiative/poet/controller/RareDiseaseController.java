package org.monarchinitiative.poet.controller;
import org.monarchinitiative.poet.model.entities.RareDiseaseAnnotation;
import org.monarchinitiative.poet.service.AnnotationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/annotation/rare/") // /annotation/<type>/<action>
public class RareDiseaseController {

    private final AnnotationService annotationService;

    public RareDiseaseController(final AnnotationService annotationService){
        this.annotationService = annotationService;
    }

    /**
     *
     * @param annotation - an annotation
     * @return created
     */
    @PostMapping(value = "/", headers = "Accept=application/json")
    public RareDiseaseAnnotation newRareAnnotation(@RequestBody RareDiseaseAnnotation annotation) {
        return annotationService.createRareDiseaseAnnotation(annotation);
    }

    /**
     *
     * @param query - a string to find a disease annotation by id.
     * @return the rare disease annotation or nothing
     */
    @GetMapping(value = "/", headers = "Accept=application/json")
    public RareDiseaseAnnotation getRareAnnotationByDisease(@RequestParam String query) {
        return annotationService.getRareDiseaseAnnotation("ha");
    }
}
