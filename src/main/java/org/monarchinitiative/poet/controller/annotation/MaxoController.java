package org.monarchinitiative.poet.controller.annotation;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.exceptions.AnnotationSourceException;
import org.monarchinitiative.poet.model.MaxoRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.service.AnnotationService;
import org.monarchinitiative.poet.views.AnnotationViews;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/annotation/maxo") // /annotation/<type>/<action>
public class MaxoController {

    private AnnotationService annotationService;

    public MaxoController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @JsonView(AnnotationViews.Simple.class)
    @GetMapping(value = {"/{diseaseId}", "/{diseaseId}/{publicationId}"})
    public List<MaxoAnnotation> getMaxoAnnotation(@PathVariable("diseaseId")  String diseaseId,
                                                  @PathVariable(value = "publicationId", required = false) String publicationId,
                                                  @RequestParam(defaultValue = "desc date") String sort){

        final List<MaxoAnnotation> annotations = this.annotationService.getMaxoAnnotation(diseaseId, publicationId, sort);
        if(annotations != null){
            return annotations;
        } else {
            throw new AnnotationSourceException(publicationId, diseaseId);
        }
    }

    /**
     *
     * @param maxoRequest - a maxo annotation object
     * @return created
     */
    @PostMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> createMaxoAnnotation(@RequestBody MaxoRequest maxoRequest, Authentication authentication) {
        if(annotationService.createMaxoAnnotation(maxoRequest, authentication)){
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/insert", headers = "Accept=application/json")
    public void insert(){
        annotationService.insertTestData();
    }
}
