package org.monarchinitiative.poet.controller.annotation;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.entities.PhenotypeAnnotation;
import org.monarchinitiative.poet.service.AnnotationService;
import org.monarchinitiative.poet.views.AnnotationViews;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "${api.version}/annotation/phenotypes")
public class PhenotypeController {

    private AnnotationService annotationService;

    public PhenotypeController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @JsonView(AnnotationViews.Simple.class)
    @GetMapping(value = {"/{diseaseId}", "/{diseaseId}/{publicationId}"})
    public List<PhenotypeAnnotation> getPhenotypeAnnotation(@PathVariable("diseaseId")  String diseaseId,
                                                            @PathVariable(value = "publicationId", required = false) String publicationId,
                                                            @RequestParam(defaultValue = "desc date") String sort){

        return Collections.emptyList();
    }
}
