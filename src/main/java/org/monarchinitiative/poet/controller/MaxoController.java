package org.monarchinitiative.poet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.exceptions.AnnotationSourceException;
import org.monarchinitiative.poet.model.MaxoRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.repository.AnnotationSourceRepository;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PublicationRepository;
import org.monarchinitiative.poet.service.AnnotationService;
import org.monarchinitiative.poet.views.AnnotationViews;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @JsonView(AnnotationViews.Simple.class)
    @GetMapping("/{publicationId}/{diseaseId}")
    public List<MaxoAnnotation> getMaxoAnnotation(@PathVariable("publicationId") String publicationId,
                                                  @PathVariable("diseaseId")  String diseaseId,
                                                  @RequestParam(defaultValue = "desc date") String sort){
        final List<MaxoAnnotation> annotations = this.annotationService.getMaxoAnnotation(publicationId, diseaseId, sort);
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
    public ResponseEntity<?> createMaxoAnnotation(@RequestBody MaxoRequest maxoRequest) {
        if(annotationService.createMaxoAnnotation(maxoRequest)){
         return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/insert", headers = "Accept=application/json")
    public void insert(){
        Publication publication = new Publication("PMID:31479590", "Encoding Clinical Data with the Human Phenotype Ontology for Computational Differential Diagnostics.", "2019 Sept", "Kohler S");
        Publication publication2 = new Publication("PMID:30476213", "Expansion of the Human Phenotype Ontology (HPO) knowledge base and resources", "2019 Jan", "Kohler S");
        Publication publication3 = new Publication("PMID:30323234", "Mikes future first author paper", "2020 Jan", "Gargano M");
        publicationRepository.save(publication);
        publicationRepository.save(publication2);
        publicationRepository.save(publication3);
        Disease disease = new Disease("OMIM:154700", "Marfan Syndrome");
        Disease disease2 = new Disease("OMIM:300200", "Adrenal Hypoplasia, Congenital");
        diseaseRepository.save(disease);
        diseaseRepository.save(disease2);
        annotationSourceRepository.save(new AnnotationSource(publication3, disease));
        annotationSourceRepository.save(new AnnotationSource(publication, disease));
        annotationSourceRepository.save(new AnnotationSource(publication2, disease));
    }
}
