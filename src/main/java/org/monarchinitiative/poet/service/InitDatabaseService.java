package org.monarchinitiative.poet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.WordUtils;
import org.monarchinitiative.poet.exceptions.DuplicateAnnotationException;
import org.monarchinitiative.poet.model.utility.HpoAnnotationLine;
import org.monarchinitiative.phenol.base.PhenolException;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.model.requests.PhenotypeRequest;
import org.monarchinitiative.poet.model.requests.PublicationRequest;
import org.monarchinitiative.poet.utility.ContainsPubmed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.*;

@Service
public class InitDatabaseService {

    private final EntityService entityService;
    private final AnnotationService annotationService;
    private final UserService userService;

    InitDatabaseService(AnnotationService annotationService,
                 EntityService entityService, UserService userService){
        this.entityService = entityService;
        this.annotationService = annotationService;
        this.userService = userService;
    }

    @Value("${initializePoet}")
    private boolean shouldLoad;

    @Value("classpath:phenotype.hpoa")
    private Resource hpoaResource;

    @Value("classpath:hp.obo")
    private Resource oboResource;

    //Run this method when application started
    @EventListener(ApplicationReadyEvent.class)
    public void loadData() throws IOException, PhenolException {
        // if property has load data flag
        if(shouldLoad) {
            System.out.println("Staring POET Initialization.");
            User user = userService.getHumanPhenotypeOntologyUser();
            Ontology ontology = OntologyLoader.loadOntology(new File(oboResource.getURI()));
            Map<TermId, Term> ontologyMap = ontology.getTermMap();
            Map<TermId, List<HpoAnnotationLine>> diseaseAnnotationMap = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader(new File(hpoaResource.getURI())))) {
                String line = br.readLine();
                while (line.startsWith("#")) {
                    line = br.readLine();
                } // this skips the comments (including the definition of the header)
                while ((line = br.readLine()) != null) {
                    HpoAnnotationLine annotationLine = HpoAnnotationLine.constructFromString(line);
                    if (annotationLine.hasValidNumberOfFields()) {
                        TermId diseaseId = annotationLine.getDiseaseTermId();
                        diseaseAnnotationMap.putIfAbsent(diseaseId, new ArrayList<>());
                        diseaseAnnotationMap.get(diseaseId).add(annotationLine);
                    }
                }
            } catch (IOException | PhenolException e) {
                throw new PhenolException(String.format("Could not read annotation file: %s", e.getMessage()));
            }

            diseaseAnnotationMap.entrySet().forEach(entry -> {
                Optional<String> diseaseNameOptional = entry.getValue().stream().map(
                        line -> line.getDbObjectName().replaceAll("\\d{6}|^#\\d{6}|^\\d{6}|^\\+\\d{6}", "").trim().split(";")[0]).min(Comparator.comparingInt(String::length));
                final String diseaseId = entry.getKey().toString();
                final Disease disease;
                if(diseaseNameOptional.isPresent()){
                    if(diseaseId.contains("OMIM")){
                        final String diseaseName = WordUtils.capitalizeFully(diseaseNameOptional.get());
                        disease = new Disease(diseaseId.toString(), diseaseName);
                        entityService.saveNewDisease(disease);
                    } else {
                        return;
                    }
                } else {
                    return;
                }

                entry.getValue().forEach(phenotype -> {
                    Term term = ontologyMap.get(phenotype.getPhenotypeId());
                    ContainsPubmed<String> cpm = new ContainsPubmed<>();
                    List<String> publications = phenotype.getPublications();
                    AnnotationSource annotationSource = new AnnotationSource();
                    // Use the first Publication Identifier for now until we scale out the model
                    Optional<String> publicationCitation = publications.stream().filter(citation -> citation.contains("PMID")).findFirst();
                    Optional<String> diseaseCitation = publications.stream().filter(citation -> citation.contains("OMIM")).findFirst();
                    Optional<String> isbnCitation = publications.stream().filter(citation -> citation.contains("ISBN")).findFirst();
                    if(publicationCitation.isPresent()){
                        // Phenotype has PMID create source if not exist
                        Publication publication = null;
                        annotationSource = entityService.getAnnotationSource(publicationCitation.get(), disease.getDiseaseId());
                        if(annotationSource == null){
                            try {
                                publication = this.getPublication(publicationCitation.get());
                            } catch (IOException e) {
                                publication = new Publication(publicationCitation.get(), "", "","");
                            }
                            annotationSource = entityService.createAnnotationSource(new PublicationRequest(publication, disease));
                        }
                    } else if(diseaseCitation.isPresent()){
                        annotationSource = entityService.getAnnotationSource(disease.getDiseaseId(), disease.getDiseaseId());
                        if(annotationSource == null){
                            Publication publication = new Publication(diseaseId, "https://www.omim.org/", "", "");
                            annotationSource = entityService.createAnnotationSource(new PublicationRequest(publication, disease));
                        }
                    } else if(isbnCitation.isPresent() || publications.size() == 1){
                        final String publicationId = isbnCitation.orElseGet(() -> publications.get(0));
                        Publication publication = new Publication(publicationId, "", "", "");
                        annotationSource = entityService.getAnnotationSource(publication.getPublicationId(), disease.getDiseaseId());
                        if(annotationSource == null){
                            annotationSource = entityService.createAnnotationSource(new PublicationRequest(publication, disease));
                        }
                    } else {
                        System.out.println(diseaseId);
                    }

                    PhenotypeRequest request = new PhenotypeRequest(null, term.getName(), term.getId().toString(),
                            phenotype.getEvidence(), "", phenotype.getOnsetId(),
                            annotationSource.getPublication().getPublicationId(),
                    annotationSource.getPublication().getPublicationName(),
                            annotationSource.getDisease().getDiseaseId(),
                            annotationSource.getDisease().getDiseaseName(),
                            phenotype.getModifierList(),
                            phenotype. getFrequency(), phenotype.isNOT() ? "NOT": "", phenotype.getSex(), null);
                    try{
                        annotationService.createPhenotypeAnnotation(request, user);
                    } catch (DuplicateAnnotationException ex){
                        System.out.println(ex.getMessage());
                    }
                });
            });
            System.out.println("Finished POET Initialization.");
        } else {
            System.out.println("Skipping POET Initialization.");
        }
    }

   private Publication getPublication(String publicationId) throws IOException {
        publicationId = publicationId.replace("PMID:", "");
        final String urlString = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi" + "?db=pubmed&retmode=json&id=" + publicationId;
        URL url = new URL(urlString);
        // Convert to a JSON object to print data
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(url, Map.class);
        Map result = (Map) map.get("result");
        Map publication = (Map) result.get(publicationId);
        return new Publication("PMID:" + publicationId, (String) publication.get("title"),
                (String) publication.get("pubdate"), (String) publication.get("sortfirstauthor"));
    }
}

