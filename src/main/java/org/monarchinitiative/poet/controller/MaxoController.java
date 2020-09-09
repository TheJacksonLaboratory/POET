package org.monarchinitiative.poet.controller;

import org.monarchinitiative.poet.model.MaxoRequest;
import org.monarchinitiative.poet.model.entities.MaxoAnnotation;
import org.monarchinitiative.poet.model.entities.RareDiseaseAnnotation;
import org.monarchinitiative.poet.service.AnnotationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/annotation/maxo") // /annotation/<type>/<action>
public class MaxoController {

    private final AnnotationService annotationService;

    public MaxoController(AnnotationService annotationService){
        this.annotationService = annotationService;
    }

    /**
     *
     * @param maxoRequest - a maxo request
     * @return created
     */
    @PostMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity newMaxoAnnotation(@RequestBody MaxoRequest maxoRequest) {
        if(annotationService.createMaxoAnnotation(maxoRequest)){
         return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
