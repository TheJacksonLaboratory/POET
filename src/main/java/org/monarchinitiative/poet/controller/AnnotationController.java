package org.monarchinitiative.poet.controller;

import org.monarchinitiative.poet.model.MaxoAnnotation;
import org.monarchinitiative.poet.model.RareDiseaseAnnotation;
import org.monarchinitiative.poet.service.AnnotationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/annotation") // /annotation/<type>/<action>
public class AnnotationController {

    private final AnnotationService annotationService;

    public AnnotationController(final AnnotationService annotationService){
     this.annotationService = annotationService;
    }

    /**
     *
     * @param annotation - an annotation
     * @return created
     */
    @PostMapping(value = "/rare", headers = "Accept=application/json")
    public RareDiseaseAnnotation newRareAnnotation(@RequestBody RareDiseaseAnnotation annotation) {
        return annotationService.createRareAnnotation(annotation);
    }

    /**
     *
     * @param annotation - an annotation
     * @return created
     */
    @PostMapping(value = "/maxo", headers = "Accept=application/json")
    public MaxoAnnotation newMaxoAnnotation(@RequestBody MaxoAnnotation annotation) {
        return annotationService.createMaxoAnnotation(annotation);
    }

    /**
     *
     * @return created
     */
    @GetMapping(value = "/test", headers = "Accept=application/json")
    public void test() {
        //return annotationService.createAnnotation(annotation);
    }
}
