package org.monarchinitiative.poet.controller;

import org.monarchinitiative.poet.model.MaxoRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.repository.AnnotationSourceRepository;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.monarchinitiative.poet.service.AnnotationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/annotation/maxo") // /annotation/<type>/<action>
public class MaxoController {

    private AnnotationService annotationService;
    private AnnotationSourceRepository annotationSourceRepository;
    private PublicationRepository publicationRepository;
    private DiseaseRepository diseaseRepository;

    public MaxoController(AnnotationService annotationService, AnnotationSourceRepository annotationSourceRepository, PublicationRepository publicationRepository, DiseaseRepository diseaseRepository) {
        this.annotationService = annotationService;
        this.annotationSourceRepository = annotationSourceRepository;
        this.publicationRepository = publicationRepository;
        this.diseaseRepository = diseaseRepository;
    }

    /**
     *
     * @param maxoRequest - a maxo request
     * @return created
     */
    @PostMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> newMaxoAnnotation(@RequestBody MaxoRequest maxoRequest) {
        if(annotationService.createMaxoAnnotation(maxoRequest)){
         return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/insert", headers = "Accept=application/json")
    public void insert(){
        Publication publication = new Publication("31479590", "Encoding Clinical Data with the Human Phenotype Ontology for Computational Differential Diagnostics.", "2019 Sept", "Kohler S");
        Publication publication2 = new Publication("'30476213'", "Expansion of the Human Phenotype Ontology (HPO) knowledge base and resources", "2019 Jan", "Kohler S");
        publicationRepository.save(publication);
        publicationRepository.save(publication2);
        Disease disease = new Disease("OMIM:154700", "Marfan Syndrome");
        Disease disease2 = new Disease("OMIM:300200", "Adrenal Hypoplasia, Congenital");
        diseaseRepository.save(disease);
        diseaseRepository.save(disease2);
        AnnotationSource source = new AnnotationSource(publication, disease);
        AnnotationSource source2 = new AnnotationSource(publication2, disease);
        annotationSourceRepository.save(source);
        annotationSourceRepository.save(source2);
    }
}
